import React, { useEffect, useState } from 'react';
import { getProducts, createProduct } from '../services/productService';
import { useAuth } from '../store/AuthContext';
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';
import { toast } from 'react-toastify'; // Profesyonel bildirimler için

export const Products = () => {
    const [products, setProducts] = useState<any[]>([]);
    const { token } = useAuth();
    const [showModal, setShowModal] = useState(false);
    const [newProduct, setNewProduct] = useState({
        name: '',
        sku: '',
        price: 0,
        stockQuantity: 0,
        active: true
    });

    useEffect(() => {
        fetchProducts();

        // --- WebSocket Kurulumu ---
        const socket = new SockJS('http://localhost:8080/ws-inventory');
        const stompClient = new Client({
            webSocketFactory: () => socket,
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            onConnect: () => {
                console.log('✅ WebSocket Bağlantısı Başarılı!');
                
                // Kritik stok kanalına abone ol
                stompClient.subscribe('/topic/stock-alerts', (message: IMessage) => {
                    const alertData = JSON.parse(message.body);
                    
                    // 1. Şık Bildirim Göster
                    toast.error(`🚨 KRİTİK STOK: ${alertData.productName} (Kalan: ${alertData.currentStock})`, {
                        position: "top-right",
                        autoClose: 5000,
                        theme: "colored"
                    });

                    // 2. Tabloyu Sayfa Yenilenmeden Güncelle
                    setProducts(prevProducts => 
                        prevProducts.map(p => 
                            p.sku === alertData.sku 
                            ? { ...p, stockQuantity: alertData.currentStock } 
                            : p
                        )
                    );
                });
            },
            onStompError: (frame) => {
                console.error('❌ STOMP Hatası:', frame.headers['message']);
            }
        });

        stompClient.activate();

        return () => {
            if (stompClient.active) stompClient.deactivate();
        };
    }, [token]);

    const fetchProducts = async () => {
        try {
            const res = await getProducts();
            setProducts(res.data);
        } catch (error) {
            console.error("Ürünler yüklenirken hata oluştu:", error);
        }
    };

    const handleSave = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const { active, ...dataToSend } = newProduct;
            await createProduct(dataToSend);
            toast.success("Ürün başarıyla eklendi!");
            setShowModal(false);
            fetchProducts();
        } catch {
            toast.error("Hata: Ürün eklenemedi.");
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h2>📦 Ürün Yönetimi</h2>
                <button
                    onClick={() => setShowModal(true)}
                    style={{ padding: '10px 20px', backgroundColor: '#1a202c', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                >
                    + Yeni Ürün Ekle
                </button>
            </div>

            <div style={{ background: 'white', borderRadius: '8px', boxShadow: '0 2px 10px rgba(0,0,0,0.05)', overflow: 'hidden' }}>
                <table style={{ width: '100%', textAlign: 'left', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr style={{ backgroundColor: '#f8fafc', borderBottom: '2px solid #edf2f7' }}>
                            <th style={{ padding: '15px' }}>ID</th>
                            <th style={{ padding: '15px' }}>Ürün Adı</th>
                            <th style={{ padding: '15px' }}>SKU</th>
                            <th style={{ padding: '15px' }}>Fiyat</th>
                            <th style={{ padding: '15px' }}>Stok</th>
                        </tr>
                    </thead>
                    <tbody>
                        {products.map(p => (
                            <tr key={p.id} style={{ borderBottom: '1px solid #edf2f7' }}>
                                <td style={{ padding: '15px' }}>{p.id}</td>
                                <td style={{ padding: '15px', fontWeight: 'bold' }}>{p.name}</td>
                                <td style={{ padding: '15px' }}><code style={{ background: '#f1f5f9', padding: '2px 5px' }}>{p.sku}</code></td>
                                <td style={{ padding: '15px' }}>{p.price} TL</td>
                                <td style={{ 
                                    padding: '15px', 
                                    color: p.stockQuantity <= 5 ? '#e53e3e' : '#2d3748', 
                                    fontWeight: p.stockQuantity <= 5 ? 'bold' : 'normal',
                                    backgroundColor: p.stockQuantity <= 5 ? '#fff5f5' : 'transparent'
                                }}>
                                    {p.stockQuantity} {p.stockQuantity <= 5 && '⚠️'}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {showModal && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', backgroundColor: 'rgba(0,0,0,0.6)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000 }}>
                    <div style={{ backgroundColor: 'white', padding: '30px', borderRadius: '12px', width: '450px', boxShadow: '0 20px 25px rgba(0,0,0,0.2)' }}>
                        <h3 style={{ marginTop: 0 }}>Yeni Ürün Ekle</h3>
                        <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                            <input type="text" placeholder="Ürün Adı" required onChange={e => setNewProduct({ ...newProduct, name: e.target.value })} style={{ padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e0' }} />
                            <input type="text" placeholder="SKU (Barkod)" required onChange={e => setNewProduct({ ...newProduct, sku: e.target.value })} style={{ padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e0' }} />
                            <input type="number" placeholder="Fiyat" required onChange={e => setNewProduct({ ...newProduct, price: Number(e.target.value) })} style={{ padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e0' }} />
                            <input type="number" placeholder="Stok Miktarı" required onChange={e => setNewProduct({ ...newProduct, stockQuantity: Number(e.target.value) })} style={{ padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e0' }} />
                            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '10px' }}>
                                <button type="button" onClick={() => setShowModal(false)} style={{ padding: '10px 20px', background: '#e2e8f0', border: 'none', borderRadius: '6px', cursor: 'pointer' }}>İptal</button>
                                <button type="submit" style={{ padding: '10px 20px', backgroundColor: '#1A202C', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}>Ürünü Kaydet</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};