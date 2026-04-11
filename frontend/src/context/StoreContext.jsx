import { createContext, useState, useEffect } from 'react';

export const StoreContext = createContext();

export const StoreProvider = ({ children }) => {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);

  // Generate 100 Mock Data
  useEffect(() => {
    const generateMockProducts = () => {
      const mockData = [];
      const categories = [
        { name: 'Figures', img: '/images/figure.png', prefix: 'Premium Scale Figure' },
        { name: 'Manga', img: '/images/manga.png', prefix: 'Manga Volume' },
        { name: 'Apparel', img: '/images/apparel.png', prefix: 'Cyber Streetwear' }
      ];

      for (let i = 1; i <= 100; i++) {
        const category = categories[i % 3];
        let price = 0;
        if(category.name === 'Figures') price = 2500000 + (Math.random() * 500000);
        else if(category.name === 'Manga') price = 55000 + (Math.random() * 20000);
        else price = 350000 + (Math.random() * 250000);

        mockData.push({
          id: i,
          name: `${category.prefix} Vol. ${i}`,
          description: `High quality authentic merchandise. Category: ${category.name}. Limitied stock available!`,
          price: Math.floor(price),
          category: category.name,
          imageUrl: category.img
        });
      }
      return mockData;
    };

    setProducts(generateMockProducts());
  }, []);

  const addToCart = (product) => {
    setCart(prev => {
      const existing = prev.find(item => item.id === product.id);
      if (existing) {
        return prev.map(item => item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item);
      }
      return [...prev, { ...product, quantity: 1 }];
    });
  };

  const removeFromCart = (id) => {
    setCart(prev => prev.filter(item => item.id !== id));
  };

  const updateQuantity = (id, newQty) => {
    if (newQty < 1) return;
    setCart(prev => prev.map(item => item.id === id ? { ...item, quantity: newQty } : item));
  };

  const clearCart = () => setCart([]);

  const value = {
    products,
    cart,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart
  };

  return (
    <StoreContext.Provider value={value}>
      {children}
    </StoreContext.Provider>
  );
};
