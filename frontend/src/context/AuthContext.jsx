import { createContext, useState } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const login = (email, password) => {
    // Simulasi login — nanti akan terhubung ke backend /api/auth/login
    setUser({
      id: 1,
      name: email.split('@')[0],
      email: email,
      role: 'Customer'
    });
    setIsAuthenticated(true);
    return true;
  };

  const register = (name, email, password) => {
    // Simulasi register — nanti akan terhubung ke backend /api/auth/register
    setUser({
      id: Date.now(),
      name: name,
      email: email,
      role: 'Customer'
    });
    setIsAuthenticated(true);
    return true;
  };

  const logout = () => {
    setUser(null);
    setIsAuthenticated(false);
  };

  const value = {
    user,
    isAuthenticated,
    login,
    register,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
