import { useState, useRef, useContext, useEffect, useCallback } from 'react';
import { StoreContext } from '../context/StoreContext';

const APPAREL_TYPES = [
  { id: 'tshirt',     name: 'T-Shirt',    img: '/images/outfit_tshirt.png',     backImg: '/images/outfit_tshirt_back.png' },
  { id: 'hoodie',     name: 'Hoodie',     img: '/images/outfit_hoodie.png',     backImg: '/images/outfit_hoodie_back.png' },
  { id: 'polo',       name: 'Polo',       img: '/images/outfit_polo.png',       backImg: '/images/outfit_polo_back.png' },
  { id: 'jacket',     name: 'Jacket',     img: '/images/outfit_jacket.png',     backImg: '/images/outfit_jacket_back.png' },
  { id: 'longsleeve', name: 'Long Sleeve',img: '/images/outfit_longsleeve.png', backImg: '/images/outfit_longsleeve_back.png' },
];

const PRESET_COLORS = [
  { name: 'White', hex: '#FFFFFF', filter: 'brightness(1)',   canvasBg: '#000000' },
  { name: 'Black', hex: '#111111', filter: 'brightness(0.2)', canvasBg: '#FFFFFF' },
];

const CustomOutfit = () => {
  const { addToCart } = useContext(StoreContext);
  
  // Studio States
  const [activeType, setActiveType] = useState(APPAREL_TYPES[0]);
  const [activeColor, setActiveColor] = useState(PRESET_COLORS[0]);
  const [viewSide, setViewSide] = useState('front'); // 'front' | 'back'
  const [logoUrl, setLogoUrl] = useState(null);
  const [logoScale, setLogoScale] = useState(1);
  // Position as percentage of canvas size so it stays centered regardless of window size
  const [designPos, setDesignPos] = useState({ xPct: 50, yPct: 28 }); // chest-center ~28% from top
  const [isDragging, setIsDragging] = useState(false);
  
  const dragStart = useRef({ x: 0, y: 0 });
  const canvasRef = useRef(null);
  const fileInputRef = useRef(null);

  // Reset view & overlay position when apparel type changes
  const handleTypeChange = useCallback((type) => {
    setActiveType(type);
    setViewSide('front');
    setDesignPos({ xPct: 50, yPct: 28 });
  }, []);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const url = URL.createObjectURL(file);
      setLogoUrl(url);
    }
  };

  const handlePointerDown = (e) => {
    if (e.button !== 0) return;
    setIsDragging(true);
    e.currentTarget.setPointerCapture(e.pointerId);
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    dragStart.current = {
      xOff: e.clientX - (designPos.xPct / 100) * rect.width,
      yOff: e.clientY - (designPos.yPct / 100) * rect.height,
    };
  };

  const handlePointerMove = (e) => {
    if (!isDragging) return;
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const newX = ((e.clientX - dragStart.current.xOff) / rect.width) * 100;
    const newY = ((e.clientY - dragStart.current.yOff) / rect.height) * 100;
    setDesignPos({ xPct: newX, yPct: newY });
  };

  const handlePointerUp = (e) => {
    setIsDragging(false);
    e.currentTarget.releasePointerCapture(e.pointerId);
  };

  const handleAddToCart = () => {
    const customItem = {
      id: `custom-${Date.now()}`,
      name: `Custom ${activeType.name} (${activeColor.name})`,
      price: 450000,
      category: 'Apparel',
      imageUrl: activeType.img,
      customOptions: {
        color: activeColor.hex,
        type: activeType.id,
        logo: logoUrl ? 'Uploaded Design' : 'No Logo'
      }
    };
    addToCart(customItem);
    alert('Custom creation added to your cart!');
  };

  return (
    <div className="page-container">
      <div className="outfit-header">
        <h2>Custom Outfit <span className="accent-text">Studio</span></h2>
        <p className="subtitle">Conceptualize your unique Otaku-inspired apparel in realtime.</p>
      </div>

      <div className="editor-workspace">
        {/* Left Panel: Controls */}
        <div className="controls-panel">
          
          <div className="control-group">
            <h3>1. Base Apparel</h3>
            <div className="type-grid">
              {APPAREL_TYPES.map(type => (
                <button 
                  key={type.id}
                  className={`type-btn ${activeType.id === type.id ? 'type-btn-active' : ''}`}
                  onClick={() => handleTypeChange(type)}
                >
                  <img src={type.img} alt={type.name} />
                  <span>{type.name}</span>
                </button>
              ))}
            </div>
          </div>

          <div className="control-group">
            <h3>2. Material Color</h3>
            <div className="color-duo">
              {PRESET_COLORS.map(color => (
                <button
                  key={color.name}
                  className={`color-duo-btn ${activeColor.name === color.name ? 'color-duo-active' : ''}`}
                  style={{
                    backgroundColor: color.hex,
                    color: color.name === 'White' ? '#111' : '#fff',
                    borderColor: activeColor.name === color.name
                      ? (color.name === 'White' ? '#fff' : '#000')
                      : 'var(--glass-border)'
                  }}
                  onClick={() => setActiveColor(color)}
                >
                  {color.name}
                  {activeColor.name === color.name && <span className="color-check">✓</span>}
                </button>
              ))}
            </div>
          </div>

          <div className="control-group">
            <h3>3. Design Overlay</h3>
            <input 
              type="file" 
              ref={fileInputRef} 
              onChange={handleFileUpload} 
              accept="image/*" 
              className="hidden-input" 
            />
            <div className="upload-trigger" onClick={(e) => { e.stopPropagation(); fileInputRef.current.click(); }}>
              <span>{logoUrl ? 'Change Design' : 'Upload Design (.png/.jpg)'}</span>
            </div>
          </div>

          {logoUrl && (
            <div className="control-group animate-fade-in">
              <h3>4. Design Scale ({Math.round(logoScale * 100)}%)</h3>
              <input 
                type="range" 
                min="0.2" 
                max="2" 
                step="0.05" 
                value={logoScale} 
                onChange={(e) => setLogoScale(parseFloat(e.target.value))}
                className="studio-slider"
              />
            </div>
          )}

          <div className="control-group" style={{ marginTop: 'auto' }}>
            <button className="btn-accent" onClick={handleAddToCart}>
              Add Custom Creation
            </button>
          </div>
        </div>

        {/* Right Panel: Canvas */}
        <div className="canvas-container">
          {/* Front / Back toggle */}
          <div className="view-toggle">
            <button
              className={`view-btn ${viewSide === 'front' ? 'view-btn-active' : ''}`}
              onClick={() => setViewSide('front')}
            >Front</button>
            <button
              className={`view-btn ${viewSide === 'back' ? 'view-btn-active' : ''}`}
              onClick={() => setViewSide('back')}
            >Back</button>
          </div>

          <div className="apparel-visualizer" ref={canvasRef}
            style={{ background: activeColor.canvasBg, transition: 'background 0.4s ease' }}
            onPointerMove={handlePointerMove}
            onPointerUp={handlePointerUp}
            onPointerCancel={handlePointerUp}
          >
            <img 
              src={viewSide === 'front' ? activeType.img : activeType.backImg} 
              alt={`Apparel ${viewSide}`}
              className="apparel-base" 
              style={{ filter: activeColor.filter }}
            />
            
            {/* Design overlay — percentage-positioned so it always lands on the chest */}
            <div 
              className="draggable-logo-v2"
              style={{
                left: `${designPos.xPct}%`,
                top: `${designPos.yPct}%`,
                transform: 'translate(-50%, -50%)',
                width: `${120 * logoScale}px`,
                height: `${120 * logoScale}px`,
                position: 'absolute',
                cursor: isDragging ? 'grabbing' : 'grab'
              }}
              onPointerDown={handlePointerDown}
            >
              {logoUrl ? (
                <img src={logoUrl} alt="Custom Logo" className="logo-img" />
              ) : (
                <div className="no-logo-hint">DRAG TO POSITION</div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomOutfit;
