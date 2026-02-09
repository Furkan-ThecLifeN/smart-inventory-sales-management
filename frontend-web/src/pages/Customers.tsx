import React, { useState, useEffect } from 'react';
import httpClient from '../services/httpClient';
import { Trash2 } from 'lucide-react';

export const Customers = () => {
    const [customers, setCustomers] = useState<any[]>([]);

    useEffect(() => {
        const fetchCustomers = async () => {
            try {
                const res = await httpClient.get('/customers');
                setCustomers(res.data);
            } catch (err) {
                console.error("Müşteriler yüklenemedi:", err);
            }
        };
        fetchCustomers();
    }, []);

    const handleDelete = async (id: number) => {
        if (window.confirm("Müşteriyi silmek istediğinize emin misiniz?")) {
            await httpClient.delete(`/customers/${id}`);
            setCustomers(customers.filter((c: any) => c.id !== id));
        }
    };

    return (
        <div>
            <h1>👥 Müşteri Yönetimi</h1>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px', marginTop: '20px' }}>
                {customers.map((c: any) => (
                    <div key={c.id} style={{ background: 'white', padding: '15px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
                        <h3 style={{ margin: '0 0 10px 0' }}>{c.firstName} {c.lastName}</h3>
                        <p style={{ fontSize: '14px', color: '#666' }}>📧 {c.email}</p>
                        <p style={{ fontSize: '14px', color: '#666' }}>📞 {c.phone}</p>
                        <button onClick={() => handleDelete(c.id)} style={{ marginTop: '10px', color: 'red', border: 'none', background: 'none', cursor: 'pointer' }}>
                            <Trash2 size={16} /> Sil
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};
