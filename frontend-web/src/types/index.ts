// Kullanıcı Tipleri
export interface User {
    id: number;
    username: string;
    email: string;
    roles: string[];
}

// Ürün Tipleri
export interface Product {
    id: number;
    name: string;
    sku: string;
    description?: string;
    price: number;
    stockQuantity: number;
    active: boolean;
}

// Sipariş Tipleri
export interface Order {
    id: number;
    orderNumber: string;
    totalAmount: number;
    status: 'CREATED' | 'PAID' | 'CANCELLED';
    createdAt: string;
}

// Auth Tipleri
export interface AuthResponse {
    accessToken: string;
    username: string;
    roles: string[];
}