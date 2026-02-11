import React from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { RoleGuard } from "../components/RoleGuard";
import { useAuth } from "../store/AuthContext";
import { LayoutDashboard, Package, Users, LogOut, Box } from "lucide-react"; // npm install lucide-react

export const MainLayout: React.FC = () => {
  const { logout } = useAuth();
  const location = useLocation();

  // Aktif linki belirlemek için yardımcı stil fonksiyonu
  const getLinkStyle = (path: string) => ({
    display: "flex",
    alignItems: "center",
    gap: "12px",
    padding: "12px 16px",
    borderRadius: "8px",
    color: location.pathname === path ? "#fff" : "#a0aec0",
    background: location.pathname === path ? "#2d3748" : "transparent",
    textDecoration: "none",
    transition: "all 0.3s ease",
    fontWeight: location.pathname === path ? "600" : "400",
  });

  return (
    <div
      style={{
        display: "flex",
        height: "100vh",
        fontFamily: "'Inter', sans-serif",
      }}
    >
      {/* Sidebar */}
      <aside
        style={{
          width: 280,
          background: "#1a202c",
          color: "white",
          padding: "24px",
          display: "flex",
          flexDirection: "column",
          boxShadow: "4px 0 10px rgba(0,0,0,0.1)",
        }}
      >
        <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: "10px",
            marginBottom: "40px",
          }}
        >
          <Box size={32} color="#63b3ed" />
          <h2
            style={{
              fontSize: "1.25rem",
              fontWeight: "bold",
              margin: 0,
              letterSpacing: "-0.5px",
            }}
          >
            Smart Inventory
          </h2>
        </div>

        <nav
          style={{
            display: "flex",
            flexDirection: "column",
            gap: "8px",
            flex: 1,
          }}
        >
          <Link to="/dashboard" style={getLinkStyle("/dashboard") as any}>
            <LayoutDashboard size={20} />
            Dashboard
          </Link>

          <RoleGuard
            allowedRoles={["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SALES"]}
          >
            <Link to="/products" style={getLinkStyle("/products") as any}>
              <Package size={20} />
              Ürün Yönetimi
            </Link>
          </RoleGuard>

          <RoleGuard allowedRoles={["ROLE_ADMIN", "ROLE_SALES"]}>
            <Link to="/customers" style={getLinkStyle("/customers") as any}>
              <Users size={20} />
              Müşteri Yönetimi
            </Link>
          </RoleGuard>
        </nav>

        <div
          style={{
            marginTop: "auto",
            paddingTop: "20px",
            borderTop: "1px solid #2d3748",
          }}
        >
          <button
            onClick={logout}
            style={{
              display: "flex",
              alignItems: "center",
              gap: "12px",
              width: "100%",
              padding: "12px 16px",
              borderRadius: "8px",
              color: "#feb2b2",
              background: "rgba(254, 178, 178, 0.1)",
              border: "none",
              cursor: "pointer",
              transition: "background 0.3s ease",
              fontSize: "1rem",
            }}
            onMouseOver={(e) =>
              (e.currentTarget.style.background = "rgba(254, 178, 178, 0.2)")
            }
            onMouseOut={(e) =>
              (e.currentTarget.style.background = "rgba(254, 178, 178, 0.1)")
            }
          >
            <LogOut size={20} />
            Çıkış Yap
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main
        style={{
          flex: 1,
          padding: "40px",
          background: "#f8fafc",
          overflowY: "auto",
          position: "relative",
        }}
      >
        <div style={{ maxWidth: "1200px", margin: "0 auto" }}>
          <Outlet />
        </div>
      </main>
    </div>
  );
};
