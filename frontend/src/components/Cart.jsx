import { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { StoreContext } from '../context/StoreContext';
import { AuthContext } from '../context/AuthContext';

const Cart = () => {
  const { cart, removeFromCart, clearCart, updateQuantity } = useContext(StoreContext);
  const { isAuthenticated } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleCheckout = () => {
    if (!isAuthenticated) {
      // Redirect ke login, simpan info bahwa user berasal dari cart
      navigate('/account', { state: { from: '/cart' } });
      return;
    }
    // User sudah login — lanjutkan checkout
    alert('Checkout berhasil! Pesanan Anda sedang diproses.');
    clearCart();
    navigate('/');
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR' }).format(price);
  };

  const subtotal = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);

  if (cart.length === 0) {
    return (
      <div className="page-container">
        <div className="cart-empty">
          <div className="cart-empty-icon">🛒</div>
          <h2>Keranjang Anda Kosong</h2>
          <p className="subtitle">Belum ada barang yang ditambahkan ke keranjang.</p>
          <Link to="/" className="btn-accent" style={{ display: 'inline-block', maxWidth: '300px', textAlign: 'center', textDecoration: 'none' }}>
            Mulai Belanja
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="cart-header">
        <h2>Shopping Cart</h2>
        <p className="subtitle">{totalItems} item dalam keranjang</p>
      </div>

      <div className="cart-layout">
        {/* Cart Items List */}
        <div className="cart-items">
          {cart.map(item => (
            <div className="cart-item glass-card" key={item.id}>
              <div className="cart-item-image">
                <img src={item.imageUrl} alt={item.name} />
              </div>
              <div className="cart-item-details">
                <h3>{item.name}</h3>
                <span className="cart-item-category">{item.category}</span>
                <div className="cart-item-price">{formatPrice(item.price)}</div>
              </div>
              <div className="cart-item-actions">
                <div className="quantity-control">
                  <button
                    className="qty-btn"
                    onClick={() => updateQuantity(item.id, item.quantity - 1)}
                    disabled={item.quantity <= 1}
                  >−</button>
                  <span className="qty-value">{item.quantity}</span>
                  <button
                    className="qty-btn"
                    onClick={() => updateQuantity(item.id, item.quantity + 1)}
                  >+</button>
                </div>
                <div className="cart-item-subtotal">
                  {formatPrice(item.price * item.quantity)}
                </div>
                <button className="cart-remove-btn" onClick={() => removeFromCart(item.id)}>
                  ✕ Hapus
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Cart Summary Sidebar */}
        <div className="cart-summary glass-card">
          <h3>Ringkasan Pesanan</h3>
          <div className="summary-rows">
            <div className="summary-row">
              <span>Total Item</span>
              <span>{totalItems}</span>
            </div>
            <div className="summary-row">
              <span>Subtotal</span>
              <span>{formatPrice(subtotal)}</span>
            </div>
            <div className="summary-divider"></div>
            <div className="summary-row summary-total">
              <span>Total</span>
              <span>{formatPrice(subtotal)}</span>
            </div>
          </div>
          <button className="btn-accent checkout-btn" onClick={handleCheckout}>
            Proceed to Checkout
          </button>
          <button className="btn-clear" onClick={clearCart}>
            Kosongkan Keranjang
          </button>
        </div>
      </div>
    </div>
  );
};

export default Cart;
