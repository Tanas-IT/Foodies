import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem("access-token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  try {
    const decoded = jwtDecode(token);
    const now = Date.now() / 1000; // seconds

    if (decoded.exp && decoded.exp < now) {
      // Token hết hạn
      localStorage.removeItem("access-token");
      localStorage.removeItem("refresh-token");
      return <Navigate to="/login" replace />;
    }
  } catch (error) {
    // Token lỗi
    localStorage.removeItem("access-token");
    localStorage.removeItem("refresh-token");
    return <Navigate to="/login" replace />;
  }

  // Nếu token hợp lệ => cho vào
  return children;
};

export default PrivateRoute;
