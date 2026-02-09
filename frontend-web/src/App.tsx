import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { AppRouter } from "./router/AppRouter";
import { AuthProvider } from "./store/AuthContext";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRouter />
        <ToastContainer position="top-right" autoClose={5000} />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
