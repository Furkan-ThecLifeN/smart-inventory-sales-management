// src/components/RoleGuard.tsx
import { useAuth } from "../store/AuthContext";

interface RoleGuardProps {
  children: React.ReactNode;
  allowedRoles: string[]; // MainLayout'ta kullandığın isimle aynı olmalı
}

export const RoleGuard = ({ children, allowedRoles }: RoleGuardProps) => {
  const { user } = useAuth();

  // Kullanıcının rollerinden en az biri izin verilen roller listesinde var mı?
  const hasAccess = user?.roles?.some((role: string) => allowedRoles.includes(role));

  if (!hasAccess) {
    return null; // Yetki yoksa hiçbir şey gösterme
  }

  return <>{children}</>;
};