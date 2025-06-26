import React, { useState } from 'react';
import './App.css';

const API = 'http://localhost:8080';

function App() {
  // BOOK
  const [bookId, setBookId] = useState('');
  const [book, setBook] = useState(null);
  const [bookTitle, setBookTitle] = useState('');
  const [bookAuthor, setBookAuthor] = useState('');
  const [bookAmount, setBookAmount] = useState(1);
  const [editBookTitle, setEditBookTitle] = useState('');
  const [editBookAuthor, setEditBookAuthor] = useState('');
  const [editBookAmount, setEditBookAmount] = useState(1);
  const [bookEditMode, setBookEditMode] = useState(false);
  const [bookError, setBookError] = useState('');

  // USER
  const [userId, setUserId] = useState('');
  const [user, setUser] = useState(null);
  const [userName, setUserName] = useState('');
  const [editUserName, setEditUserName] = useState('');
  const [userEditMode, setUserEditMode] = useState(false);
  const [userError, setUserError] = useState('');

  // BORROW/RETURN
  const [borrowUserId, setBorrowUserId] = useState('');
  const [borrowBookId, setBorrowBookId] = useState('');
  const [returnUserId, setReturnUserId] = useState('');
  const [returnBookId, setReturnBookId] = useState('');
  const [borrowMsg, setBorrowMsg] = useState('');
  const [returnMsg, setReturnMsg] = useState('');

  // STATISTICS & SEARCH
  const [searchName, setSearchName] = useState('');
  const [borrowedByName, setBorrowedByName] = useState([]);
  const [borrowedByNameError, setBorrowedByNameError] = useState('');
  const [statistic, setStatistic] = useState([]);
  const [distinctTitles, setDistinctTitles] = useState([]);

  // BOOK CRUD
  const fetchBook = async (e) => {
    e.preventDefault();
    setBookError('');
    setBook(null);
    const res = await fetch(`${API}/book/${bookId}`);
    if (res.ok) {
      const data = await res.json();
      setBook(data);
      setBookEditMode(false);
      setBookTitle('');
      setBookAuthor('');
      setBookAmount(1);
    } else {
      setBookError('Book not found');
    }
  };
  const createBook = async (e) => {
    e.preventDefault();
    setBookError('');
    const res = await fetch(`${API}/book/create`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: bookTitle, author: bookAuthor }),
    });
    if (res.ok) {
      setBookId('');
      setBook(null);
      setBookTitle('');
      setBookAuthor('');
      setBookAmount(1);
      alert('Book created!');
    } else {
      setBookError('Failed to create book');
    }
  };
  const updateBook = async (e) => {
    e.preventDefault();
    setBookError('');
    const res = await fetch(`${API}/book/${bookId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: editBookTitle, author: editBookAuthor, amount: editBookAmount }),
    });
    if (res.ok) {
      setEditBookTitle('');
      setEditBookAuthor('');
      setEditBookAmount(1);
      setBookEditMode(false);
      fetchBook({ preventDefault: () => {} });
    } else {
      setBookError('Failed to update book');
    }
  };
  const deleteBook = async () => {
    setBookError('');
    const res = await fetch(`${API}/book/${bookId}`, { method: 'DELETE' });
    if (res.ok) {
      setBook(null);
      setBookId('');
      alert('Book deleted!');
    } else {
      setBookError('Failed to delete book');
    }
  };

  // USER CRUD
  const fetchUser = async (e) => {
    e.preventDefault();
    setUserError('');
    setUser(null);
    const res = await fetch(`${API}/user/${userId}`);
    if (res.ok) {
      const data = await res.json();
      setUser(data);
      setUserEditMode(false);
    } else {
      setUserError('User not found');
    }
  };
  const createUser = async (e) => {
    e.preventDefault();
    setUserError('');
    const res = await fetch(`${API}/user/create`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: userName }),
    });
    if (res.ok) {
      setUserId('');
      setUser(null);
      setUserName('');
      alert('User created!');
    } else {
      setUserError('Failed to create user');
    }
  };
  const updateUser = async (e) => {
    e.preventDefault();
    setUserError('');
    const res = await fetch(`${API}/user/${userId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: editUserName }),
    });
    if (res.ok) {
      setUserEditMode(false);
      fetchUser({ preventDefault: () => {} });
    } else {
      setUserError('Failed to update user');
    }
  };
  const deleteUser = async () => {
    setUserError('');
    const res = await fetch(`${API}/user/${userId}`, { method: 'DELETE' });
    if (res.ok) {
      setUser(null);
      setUserId('');
      alert('User deleted!');
    } else {
      setUserError('Failed to delete user');
    }
  };

  // BORROW/RETURN
  const borrowBook = async (e) => {
    e.preventDefault();
    setBorrowMsg('');
    const res = await fetch(`${API}/borrowing/user/${borrowUserId}/book/${borrowBookId}`, {
      method: 'POST',
    });
    if (res.ok) {
      setBorrowMsg('Book borrowed!');
      setBorrowUserId('');
      setBorrowBookId('');
    } else {
      setBorrowMsg('Failed to borrow book');
    }
  };
  const returnBook = async (e) => {
    e.preventDefault();
    setReturnMsg('');
    const res = await fetch(`${API}/borrowing/return/user/${returnUserId}/book/${returnBookId}`, {
      method: 'DELETE',
    });
    if (res.ok) {
      setReturnMsg('Book returned!');
      setReturnUserId('');
      setReturnBookId('');
    } else {
      setReturnMsg('Failed to return book');
    }
  };

  // STATISTICS & SEARCH
  const fetchBorrowedByName = async (e) => {
    e.preventDefault();
    setBorrowedByName([]);
    setBorrowedByNameError('');
    try {
      const res = await fetch(`${API}/borrowing/name`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: searchName }),
      });
      if (res.ok) {
        setBorrowedByName(await res.json());
      } else {
        setBorrowedByNameError('Not found or error');
      }
    } catch {
      setBorrowedByNameError('Network error');
    }
  };
  const fetchStatistic = async () => {
    setStatistic([]);
    const res = await fetch(`${API}/borrowing/statistic`);
    if (res.ok) setStatistic(await res.json());
  };
  const fetchDistinctTitles = async () => {
    setDistinctTitles([]);
    const res = await fetch(`${API}/borrowing/titles/distinct`);
    if (res.ok) setDistinctTitles(await res.json());
  };

  return (
    <div className="app-container">
      <h1 style={{ textAlign: 'center', color: '#3b4a6b', marginBottom: 8 }}>
        📚 Book Management System
      </h1>

      {/* BOOK SECTION */}
      <section>
        <h2>Book by ID</h2>
        <form onSubmit={fetchBook} style={{ marginBottom: 8 }}>
          <input
            value={bookId}
            onChange={(e) => setBookId(e.target.value)}
            placeholder="Enter book ID"
            required
            aria-label="Book ID"
            style={{ width: 120 }}
          />
          <button type="submit">Find</button>
        </form>
        {bookError && <div style={{ color: 'red' }}>{bookError}</div>}
        {book && !bookEditMode && (
          <div style={{
            marginBottom: 16,
            background: '#f7fafd',
            borderRadius: 10,
            boxShadow: '0 2px 8px #0001',
            padding: '18px 20px',
            display: 'inline-block',
            minWidth: 260,
          }}>
            <div style={{ fontSize: 20, marginBottom: 8 }}><span role="img" aria-label="book">📖</span> <b>{book.title}</b></div>
            <div style={{ color: '#555', marginBottom: 4 }}><b>Author:</b> {book.author}</div>
            <div style={{ color: '#2a3a5e', fontWeight: 500, marginBottom: 4 }}><b>Available:</b> {book.amount}</div>
            {book.amount_of_borrowed_books !== undefined && (
              <div style={{ color: '#5b7fff', fontWeight: 500, marginBottom: 4 }}><b>Borrowed:</b> {book.amount_of_borrowed_books}</div>
            )}
            <div style={{ marginTop: 10 }}>
              <button onClick={() => {
                setEditBookTitle(book.title);
                setEditBookAuthor(book.author);
                setEditBookAmount(book.amount);
                setBookTitle('');
                setBookAuthor('');
                setBookAmount(1);
                setBookEditMode(true);
              }}>Edit</button>
              <button onClick={deleteBook} style={{ marginLeft: 8 }}>Delete</button>
            </div>
          </div>
        )}
        {bookEditMode && (
          <form onSubmit={updateBook} style={{ marginBottom: 8 }}>
            <input
              value={editBookTitle}
              onChange={(e) => setEditBookTitle(e.target.value)}
              placeholder="Title"
              required
            />
            <input
              value={editBookAuthor}
              onChange={(e) => setEditBookAuthor(e.target.value)}
              placeholder="Author"
              required
            />
            <input
              type="number"
              min={1}
              value={editBookAmount}
              onChange={(e) => setEditBookAmount(Number(e.target.value))}
              placeholder="Amount"
              required
              style={{ width: 90 }}
            />
            <button type="submit">Save</button>
            <button type="button" onClick={() => {
              setEditBookTitle('');
              setEditBookAuthor('');
              setEditBookAmount(1);
              setBookEditMode(false);
            }} style={{ marginLeft: 8 }}>
              Cancel
            </button>
          </form>
        )}
        <form onSubmit={createBook} style={{ marginBottom: 8 }}>
          <input
            value={bookTitle}
            onChange={(e) => setBookTitle(e.target.value)}
            placeholder="Title"
            required
          />
          <input
            value={bookAuthor}
            onChange={(e) => setBookAuthor(e.target.value)}
            placeholder="Author"
            required
          />
          <button type="submit">Create new book</button>
        </form>
      </section>

      {/* USER SECTION */}
      <section>
        <h2>User by ID</h2>
        <form onSubmit={fetchUser} style={{ marginBottom: 8 }}>
          <input
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            placeholder="Enter user ID"
            required
            aria-label="User ID"
            style={{ width: 120 }}
          />
          <button type="submit">Find</button>
        </form>
        {userError && <div style={{ color: 'red' }}>{userError}</div>}
        {user && !userEditMode && (
          <div style={{
            marginBottom: 16,
            background: '#f7fafd',
            borderRadius: 10,
            boxShadow: '0 2px 8px #0001',
            padding: '18px 20px',
            display: 'inline-block',
            minWidth: 260,
          }}>
            <div style={{ fontSize: 20, marginBottom: 8 }}><span role="img" aria-label="user">🧑‍💼</span> <b>{user.name}</b></div>
            <div style={{ color: '#555', marginBottom: 4 }}><b>Membership date:</b> {(user.membershipDate ?? user.membership_date) || '-'}</div>
            {(user.borrowedBooks || user.borrowed_books) && (
              <div style={{ color: '#5b7fff', marginBottom: 4 }}>
                <b>Borrowed books:</b>
                <ul style={{ margin: '4px 0 8px 0', paddingLeft: 18 }}>
                  {(user.borrowedBooks ?? user.borrowed_books).map((b, i) => (
                    <li key={i}>{b.title ?? b}</li>
                  ))}
                </ul>
              </div>
            )}
            {(user.borrowedBooksCount ?? user.borrowed_books_count ?? user.numberOfBorrowedBooks ?? user.number_of_borrowed_books) !== undefined && (
              <div style={{ color: '#2a3a5e', fontWeight: 500, marginBottom: 4 }}><b>Borrowed books count:</b> {user.borrowedBooksCount ?? user.borrowed_books_count ?? user.numberOfBorrowedBooks ?? user.number_of_borrowed_books}</div>
            )}
            <div style={{ marginTop: 10 }}>
              <button onClick={() => {
                setEditUserName(user.name);
                setUserEditMode(true);
              }}>Edit</button>
              <button onClick={deleteUser} style={{ marginLeft: 8 }}>Delete</button>
            </div>
          </div>
        )}
        {userEditMode && (
          <form onSubmit={updateUser} style={{ marginBottom: 8 }}>
            <input
              value={editUserName}
              onChange={(e) => setEditUserName(e.target.value)}
              placeholder="Name"
              required
            />
            <button type="submit">Save</button>
            <button type="button" onClick={() => setUserEditMode(false)} style={{ marginLeft: 8 }}>
              Cancel
            </button>
          </form>
        )}
        <form onSubmit={createUser} style={{ marginBottom: 8 }}>
          <input
            value={userName}
            onChange={(e) => setUserName(e.target.value)}
            placeholder="Name"
            required
          />
          <button type="submit">Create new user</button>
        </form>
      </section>

      {/* BORROW/RETURN SECTION */}
      <section>
        <h2>Borrow/Return</h2>
        <form onSubmit={borrowBook} style={{ marginBottom: 8 }}>
          <input
            value={borrowUserId}
            onChange={(e) => { setBorrowUserId(e.target.value); setBorrowMsg(''); }}
            placeholder="User ID"
            required
            style={{ width: 100 }}
          />
          <input
            value={borrowBookId}
            onChange={(e) => { setBorrowBookId(e.target.value); setBorrowMsg(''); }}
            placeholder="Book ID"
            required
            style={{ width: 100 }}
          />
          <button type="submit">Borrow</button>
        </form>
        {borrowMsg && <div style={{ color: borrowMsg.includes('Failed') ? 'red' : 'green' }}>{borrowMsg}</div>}
        <form onSubmit={returnBook} style={{ marginBottom: 8 }}>
          <input
            value={returnUserId}
            onChange={(e) => { setReturnUserId(e.target.value); setReturnMsg(''); }}
            placeholder="User ID"
            required
            style={{ width: 100 }}
          />
          <input
            value={returnBookId}
            onChange={(e) => { setReturnBookId(e.target.value); setReturnMsg(''); }}
            placeholder="Book ID"
            required
            style={{ width: 100 }}
          />
          <button type="submit">Return</button>
        </form>
        {returnMsg && <div style={{ color: returnMsg.includes('Failed') ? 'red' : 'green' }}>{returnMsg}</div>}
      </section>

      {/* STATISTICS & SEARCH SECTION */}
      <section>
        <h2>Statistics & Search</h2>
        <form onSubmit={fetchBorrowedByName} style={{ marginBottom: 8 }}>
          <input
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
            placeholder="Enter user name"
            required
            style={{ width: 180 }}
          />
          <button type="submit">Find borrowed books by name</button>
        </form>
        {borrowedByNameError && <div style={{ color: 'red' }}>{borrowedByNameError}</div>}
        {borrowedByName.length > 0 && (
          <div style={{ marginBottom: 12 }}>
            <b>Borrowed books for '{searchName}':</b>
            <ul style={{ margin: '4px 0 8px 0', paddingLeft: 18 }}>
              {borrowedByName.map((b, i) => (
                <li key={i}>{b.title ?? b}</li>
              ))}
            </ul>
          </div>
        )}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginBottom: 16, maxWidth: 260 }}>
          <button type="button" style={{ minWidth: 220 }} onClick={fetchStatistic}>Show borrowed books statistics</button>
          <button type="button" style={{ minWidth: 220 }} onClick={fetchDistinctTitles}>Show distinct borrowed titles</button>
        </div>
        {statistic.length > 0 && (
          <div style={{ marginBottom: 12 }}>
            <b>Borrowed books statistics:</b>
            <ul style={{ margin: '4px 0 8px 0', paddingLeft: 18 }}>
              {statistic.map((s, i) => (
                <li key={i} style={{ display: 'flex', justifyContent: 'space-between', background: '#f7fafd', borderRadius: 8, padding: '6px 18px', marginBottom: 6 }}>
                  <span>{s.title}</span>
                  <b>{s.amount_of_borrowed_books}</b>
                </li>
              ))}
            </ul>
          </div>
        )}
        {distinctTitles.length > 0 && (
          <div style={{ marginBottom: 12 }}>
            <b>Distinct borrowed titles:</b>
            <ul style={{ margin: '4px 0 8px 0', paddingLeft: 18 }}>
              {distinctTitles.map((t, i) => (
                <li key={i}>{t.title ?? t}</li>
              ))}
            </ul>
          </div>
        )}
      </section>
    </div>
  );
}

export default App;
