import { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { StoreContext } from '../context/StoreContext';

const Navbar = () => {
  const { cart } = useContext(StoreContext);
  const location = useLocation();

  const cartCount = cart.reduce((total, item) => total + item.quantity, 0);

  const isActive = (path) => location.pathname === path;

  return (
    <nav className="glass-nav">
      <Link to="/" className="logo-link">
        <h1 className="logo">Otaku<span className="accent-text">Store</span></h1>
      </Link>
      <div className="nav-links">
        <Link to="/" className={isActive('/') ? 'nav-active' : ''}>Catalog</Link>
        <Link to="/custom" className={isActive('/custom') ? 'nav-active' : ''}>Custom Outfit</Link>
        <Link to="/3d-figure" className={isActive('/3d-figure') ? 'nav-active' : ''}>3D Figure</Link>
        <Link to="/account" className={isActive('/account') ? 'nav-active' : ''}>Account</Link>
        <Link to="/cart" className="cart-btn">
          🛒 Cart {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
        </Link>
      </div>
    </nav>
  );
};

export default Navbar;
