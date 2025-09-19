import { jwtDecode } from "jwt-decode";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function useAuthCheck() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const decoded = jwtDecode(token); // { exp: 1699999999, ... }
      const exp = decoded.exp * 1000; // exp là giây → đổi sang ms

      if (Date.now() >= exp) {
        // 🔴 Hết hạn
        localStorage.removeItem("access-token");
        navigate("/login");
      } else {
        // 🟢 Còn hạn → set timeout để logout đúng lúc
        const timeout = exp - Date.now();
        const timer = setTimeout(() => {
          localStorage.removeItem("access-token");
          navigate("/login");
        }, timeout);

        return () => clearTimeout(timer); // cleanup
      }
    } catch (err) {
      console.error("Invalid token", err);
      localStorage.removeItem("access-token");
      navigate("/login");
    }
  }, [navigate]);
}

export default useAuthCheck;
