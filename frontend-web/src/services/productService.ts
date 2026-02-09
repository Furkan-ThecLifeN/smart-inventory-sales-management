import httpClient from './httpClient';

export const getProducts = () => httpClient.get('/products');
export const createProduct = (productData: any) => httpClient.post('/products', productData);
export const deleteProduct = (id: number) => httpClient.delete(`/products/${id}`);