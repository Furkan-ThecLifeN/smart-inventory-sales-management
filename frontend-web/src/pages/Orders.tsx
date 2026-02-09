import React, { useState, useEffect } from 'react';
import httpClient from '../services/httpClient';

export const Orders = () => {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        httpClient.get('/orders').then(res => setOrders(res.data));
    }, []);

    return (
        <div>
            <h1>🛒 Sipariş Yönetimi</h1>
            <table style={{ width: '100%', marginTop: '20px', borderCollapse: 'collapse', backgroundColor: 'white' }}>
                <thead>
                    <tr style={{ textAlign: 'left', borderBottom: '2px solid #eee' }}>
                        <th style={{ padding: '12px' }}>Sipariş No</th>
                        <th>Toplam Tutar</th>
                        <th>Durum</th>
                        <th>Tarih</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map((o: any) => (
                        <tr key={o.id} style={{ borderBottom: '1px solid #eee' }}>
                            <td style={{ padding: '12px' }}>#{o.orderNumber}</td>
                            <td>{o.totalAmount} TL</td>
                            <td>
                                <span style={{ padding: '4px 8px', borderRadius: '4px', fontSize: '12px', background: o.status === 'CREATED' ? '#eee' : '#c6f6d5' }}>
                                    {o.status}
                                </span>
                            </td>
                            <td>{new Date(o.createdAt).toLocaleDateString()}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};