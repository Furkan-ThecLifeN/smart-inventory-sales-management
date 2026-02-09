import { useState, useEffect } from "react";
import { useWebSocket } from "../hooks/useWebSocket";
import httpClient from "../services/httpClient";
import { Package, TrendingUp, AlertTriangle, Bell } from "lucide-react";

export const Dashboard = () => {
  const [notifications, setNotifications] = useState<any[]>([]);
  const [stats, setStats] = useState({
    totalProducts: 0,
    totalValue: 0,
    lowStockCount: 0,
  });

  // 1. Sayfa açıldığında SQL verilerini (İstatistikleri) çek
  useEffect(() => {
    httpClient.get("/dashboard/stats")
      .then(res => setStats(res.data))
      .catch(err => console.error("Stats yüklenemedi", err));
  }, []);

  // 2. Canlı verileri WebSocket üzerinden dinle
  useWebSocket("/topic/stock-alerts", (message) => {
    setNotifications((prev) => [message, ...prev]);
    // Canlı veri geldiğinde istatistiği de güncelle (Refresh yapmadan)
    setStats(prev => ({ ...prev, lowStockCount: prev.lowStockCount + 1 }));
  });

  const cardStyle = {
    padding: "24px",
    borderRadius: "12px",
    background: "white",
    boxShadow: "0 4px 6px -1px rgba(0,0,0,0.1)",
    display: "flex",
    flexDirection: "column" as const,
    gap: "8px"
  };

  return (
    <div style={{ padding: 30 }}>
      <h1 style={{ marginBottom: 30, fontSize: "24px", fontWeight: "bold" }}>📊 Genel Bakış</h1>

      {/* Canlı Bildirim Paneli */}
      {notifications.length > 0 && (
        <div style={{ background: "#fff5f5", border: "1px solid #fc8181", padding: "15px", marginBottom: "25px", borderRadius: "10px" }}>
          <div style={{ display: "flex", alignItems: "center", gap: "10px", color: "#c53030", marginBottom: "10px" }}>
            <Bell size={20} />
            <strong>Canlı Stok Uyarıları</strong>
          </div>
          {notifications.map((n, i) => (
            <div key={i} style={{ fontSize: "14px", padding: "5px 0", borderBottom: "1px solid #fed7d7" }}>
              {n.message} - <strong>{n.productName}</strong> (Kalan: {n.currentStock})
            </div>
          ))}
        </div>
      )}

      {/* SQL'den Gelen Gerçek Rakamlar */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: "20px" }}>
        <div style={cardStyle}>
          <Package color="#4299e1" size={32} />
          <span style={{ color: "#718096" }}>Toplam Ürün</span>
          <strong style={{ fontSize: "28px" }}>{stats.totalProducts}</strong>
        </div>

        <div style={cardStyle}>
          <TrendingUp color="#48bb78" size={32} />
          <span style={{ color: "#718096" }}>Depo Değeri</span>
          <strong style={{ fontSize: "28px" }}>₺{stats.totalValue.toLocaleString()}</strong>
        </div>

        <div style={cardStyle}>
          <AlertTriangle color="#f6e05e" size={32} />
          <span style={{ color: "#718096" }}>Kritik Stok</span>
          <strong style={{ fontSize: "28px", color: "#e53e3e" }}>{stats.lowStockCount}</strong>
        </div>
      </div>
    </div>
  );
};