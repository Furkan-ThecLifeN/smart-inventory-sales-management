import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import httpClient from "../services/httpClient";

export const RegisterPage = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !email || !password) {
      setError("Lütfen tüm alanları doldurun.");
      return;
    }
    try {
      await httpClient.post("/auth/register", { username, email, password });
      navigate("/login");
    } catch {
      setError("Kayıt başarısız. Bilgileri kontrol edin.");
    }
  };

  return (
    <div
      style={{
        background: "white",
        padding: "40px",
        borderRadius: "12px",
        boxShadow: "0 10px 25px rgba(0,0,0,0.1)",
        width: "380px",
      }}
    >
      <h2 style={{ textAlign: "center", marginBottom: "20px" }}>Kayıt Ol</h2>
      {error && <p style={{ color: "red", textAlign: "center" }}>{error}</p>}
      <form
        onSubmit={handleRegister}
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
          type="email"
          placeholder="E-posta"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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
          }}
        >
          Kayıt Ol
        </button>
      </form>
      <p style={{ textAlign: "center", marginTop: "15px" }}>
        Zaten hesabın var mı? <Link to="/login">Giriş Yap</Link>
      </p>
    </div>
  );
};
