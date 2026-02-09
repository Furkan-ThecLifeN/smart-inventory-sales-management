import React, { useState } from "react";
import { useAuth } from "../store/AuthContext";
import { useNavigate } from "react-router-dom";
import httpClient from "../services/httpClient";

export const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(""); // Hata mesajını temizle

    if (!username || !password) {
      setError("Lütfen tüm alanları doldurun.");
      return;
    }

    try {
      const response = await httpClient.post("/auth/login", { username, password });
      console.log("Backend'den Gelen Cevap:", response.data);

      const { accessToken, username: loggedInUser, roles } = response.data;

      // Veri doğrulama
      if (!accessToken || !loggedInUser) {
        throw new Error("Eksik login verisi");
      }

      // Roller undefined gelirse boş dizi ata, string gelirse diziye çevir, dizi gelirse olduğu gibi al
      const finalRoles = Array.isArray(roles) 
        ? roles 
        : roles 
          ? [roles] 
          : [];

      // AuthContext'e temiz veriyi gönder
      login(accessToken, { 
        username: loggedInUser, 
        roles: finalRoles 
      });

      navigate("/dashboard");
    } catch (err: any) {
      console.error("Login Hatası:", err);
      setError("Giriş başarısız. Kullanıcı adı veya şifre hatalı.");
    }
  };

  return (
    <div
      style={{
        background: "white",
        padding: "40px",
        borderRadius: "12px",
        boxShadow: "0 10px 25px rgba(0,0,0,0.1)",
        width: "350px",
      }}
    >
      <h2 style={{ textAlign: "center", marginBottom: "20px" }}>Giriş Yap</h2>
      {error && <p style={{ color: "red", textAlign: "center", fontSize: "14px" }}>{error}</p>}
      <form
        onSubmit={handleSubmit}
        style={{ display: "flex", flexDirection: "column", gap: "15px" }}
      >
        <input
          type="text"
          placeholder="Kullanıcı Adı"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={{ padding: "10px", borderRadius: "4px", border: "1px solid #ddd" }}
        />
        <input
          type="password"
          placeholder="Şifre"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={{ padding: "10px", borderRadius: "4px", border: "1px solid #ddd" }}
        />
        <button
          type="submit"
          style={{
            padding: "12px",
            backgroundColor: "#1a202c",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
            fontWeight: "bold"
          }}
        >
          Giriş
        </button>
      </form>
    </div>
  );
};