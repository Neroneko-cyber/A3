import { useContext, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Account = () => {
  const { user, isAuthenticated, login, register, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();
  const redirectTo = location.state?.from || null;
  const [isLoginMode, setIsLoginMode] = useState(true);
  const [formData, setFormData] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setError('');
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.email || !formData.password) {
      setError('Email dan Password wajib diisi.');
      return;
    }
    if (!isLoginMode && !formData.name) {
      setError('Nama wajib diisi untuk registrasi.');
      return;
    }

    if (isLoginMode) {
      login(formData.email, formData.password);
    } else {
      register(formData.name, formData.email, formData.password);
    }
    setFormData({ name: '', email: '', password: '' });

    // Redirect kembali ke halaman asal (misal /cart) jika ada
    if (redirectTo) {
      navigate(redirectTo);
    }
  };

  // Logged-in view
  if (isAuthenticated && user) {
    return (
      <div className="page-container">
        <div className="account-panel glass-card">
          <div className="account-header">
            <div className="avatar-circle">{user.name.charAt(0).toUpperCase()}</div>
            <h2>Welcome, {user.name}!</h2>
            <span className="role-badge">{user.role}</span>
          </div>
          <div className="account-info">
            <div className="info-row">
              <span className="info-label">Email</span>
              <span className="info-value">{user.email}</span>
            </div>
            <div className="info-row">
              <span className="info-label">Member Since</span>
              <span className="info-value">{new Date().toLocaleDateString('id-ID')}</span>
            </div>
            <div className="info-row">
              <span className="info-label">Role</span>
              <span className="info-value">{user.role}</span>
            </div>
          </div>
          <button className="btn-logout" onClick={logout}>Logout</button>
        </div>
      </div>
    );
  }

  // Login / Register form
  return (
    <div className="page-container">
      <div className="auth-container">
        <div className="auth-panel glass-card">
          <h2>{isLoginMode ? 'Login' : 'Register'}</h2>
          <p className="auth-subtitle">
            {isLoginMode ? 'Masuk ke akun OtakuStore Anda' : 'Daftar akun baru OtakuStore'}
          </p>

          {error && <div className="auth-error">{error}</div>}

          {redirectTo && (
            <div className="auth-notice">
              🔒 Anda harus login terlebih dahulu untuk melanjutkan checkout. Barang di keranjang Anda tetap tersimpan.
            </div>
          )}

          <form onSubmit={handleSubmit} className="auth-form">
            {!isLoginMode && (
              <div className="form-group">
                <label htmlFor="name">Nama Lengkap</label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="Masukkan nama Anda"
                  className="form-input"
                />
              </div>
            )}
            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="contoh@email.com"
                className="form-input"
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Masukkan password"
                className="form-input"
              />
            </div>
            <button type="submit" className="btn-accent">{isLoginMode ? 'Login' : 'Register'}</button>
          </form>

          <p className="auth-switch">
            {isLoginMode ? 'Belum punya akun? ' : 'Sudah punya akun? '}
            <button className="switch-btn" onClick={() => { setIsLoginMode(!isLoginMode); setError(''); }}>
              {isLoginMode ? 'Daftar di sini' : 'Login di sini'}
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Account;
