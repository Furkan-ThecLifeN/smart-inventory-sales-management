import { Routes, Route, Navigate, Outlet } from "react-router-dom";
import { Login } from "../pages/Login";
import { Dashboard } from "../pages/Dashboard";
import { Products } from "../pages/Products";
import { MainLayout } from "../layouts/MainLayout";
import { AuthLayout } from "../layouts/AuthLayout";
import { useAuth } from "../store/AuthContext";
import { RegisterPage } from "../pages/RegisterPage";
import { Customers } from "../pages/Customers";

const ProtectedRoute = () => {
  const { token } = useAuth();
  const storedToken = localStorage.getItem("token");

  if (!token || !storedToken) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export const AppRouter = () => {
  return (
    <Routes>
      <Route element={<AuthLayout />}>
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<Login />} />
      </Route>

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/products" element={<Products />} />
          <Route path="/customers" element={<Customers />} />
        </Route>
      </Route>

      <Route path="/" element={<Navigate to="/dashboard" />} />
      <Route path="*" element={<Navigate to="/dashboard" />} />
    </Routes>
  );
};
