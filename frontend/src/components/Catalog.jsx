import { useContext, useState } from 'react';
import { StoreContext } from '../context/StoreContext';

const ITEMS_PER_PAGE = 9;

const Catalog = () => {
  const { products, addToCart } = useContext(StoreContext);
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedCategory, setSelectedCategory] = useState('All');

  const categories = ['All', ...new Set(products.map(p => p.category))];

  const filteredProducts = selectedCategory === 'All'
    ? products
    : products.filter(p => p.category === selectedCategory);

  const totalPages = Math.ceil(filteredProducts.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const paginatedProducts = filteredProducts.slice(startIndex, startIndex + ITEMS_PER_PAGE);

  const formatPrice = (price) => {
    return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR' }).format(price);
  };

  const handleCategoryChange = (cat) => {
    setSelectedCategory(cat);
    setCurrentPage(1);
  };

  const goToPage = (page) => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  // Generate visible page numbers (max 5 visible)
  const getPageNumbers = () => {
    const pages = [];
    let start = Math.max(1, currentPage - 2);
    let end = Math.min(totalPages, start + 4);
    if (end - start < 4) start = Math.max(1, end - 4);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  };

  return (
    <div className="page-container">
      <div className="catalog-header">
        <h2>Premium Collection</h2>
        <p className="subtitle">
          Showing {startIndex + 1}–{Math.min(startIndex + ITEMS_PER_PAGE, filteredProducts.length)} of {filteredProducts.length} items
        </p>
      </div>

      {/* Category Filter Tabs */}
      <div className="category-tabs">
        {categories.map(cat => (
          <button
            key={cat}
            className={`category-tab ${selectedCategory === cat ? 'category-tab-active' : ''}`}
            onClick={() => handleCategoryChange(cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* Product Grid */}
      <div className="catalog-grid">
        {paginatedProducts.map(product => (
          <div className="glass-card" key={product.id}>
            <div className="card-image-container">
              <img src={product.imageUrl} alt={product.name} className="product-image" loading="lazy" />
              <div className="category-badge">{product.category}</div>
            </div>
            <div className="card-content">
              <h3>{product.name}</h3>
              <p className="desc">{product.description}</p>
              <div className="price-tag">{formatPrice(product.price)}</div>
              <button className="btn-accent" onClick={() => addToCart(product)}>Add to Cart</button>
            </div>
          </div>
        ))}
      </div>

      {/* Pagination Controls */}
      {totalPages > 1 && (
        <div className="pagination">
          <button
            className="page-btn"
            onClick={() => goToPage(1)}
            disabled={currentPage === 1}
          >«</button>
          <button
            className="page-btn"
            onClick={() => goToPage(currentPage - 1)}
            disabled={currentPage === 1}
          >‹</button>

          {getPageNumbers().map(page => (
            <button
              key={page}
              className={`page-btn ${currentPage === page ? 'page-btn-active' : ''}`}
              onClick={() => goToPage(page)}
            >{page}</button>
          ))}

          <button
            className="page-btn"
            onClick={() => goToPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          >›</button>
          <button
            className="page-btn"
            onClick={() => goToPage(totalPages)}
            disabled={currentPage === totalPages}
          >»</button>
        </div>
      )}
    </div>
  );
};

export default Catalog;
