import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Catalog from './components/Catalog';
import CustomOutfit from './components/CustomOutfit';
import Account from './components/Account';
import Cart from './components/Cart';

function App() {
  return (
    <div className="app-container">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Catalog />} />
          <Route path="/custom" element={<CustomOutfit />} />
          <Route path="/account" element={<Account />} />
          <Route path="/cart" element={<Cart />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
