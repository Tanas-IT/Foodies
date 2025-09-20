import React, { useContext, useEffect, useState } from "react";
import "./MyOrders.css";
import { StoreContext } from "../../context/StoreContext";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { assets } from "../../assets/assets";
import { API_BASE_URL } from "../../config/api";
const MyOrders = () => {
  const { token, setQuantities } = useContext(StoreContext);
  const [data, setData] = useState([]);
  const navigate = useNavigate();
  const fetchOrders = async () => {
    const response = await axios.get(`${API_BASE_URL}/api/orders`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    setData(response.data.data);
  };

  useEffect(() => {
    const query = new URLSearchParams(window.location.search);
    const vnp_ResponseCode = query.get("vnp_ResponseCode"); // VNPay trả về code

    if (!vnp_ResponseCode) {
      return;
    }
    const result = axios
      .post(`${API_BASE_URL}/api/vnpay/return`, null, {
        params: Object.fromEntries(query),
      })
      .then((res) => {
        if (res.status == 200) {
          setQuantities({});
          toast.success("Payment success");
        } else {
          toast.error("Payment error");
          navigate(`/order`);
        }
      })
      .catch((error) => {
        toast.error("Payment error");
        navigate(`/order`);
      });
  }, []);

  useEffect(() => {
    if (token) {
      fetchOrders();
    }
  }, [token]);
  return (
    <div className="container">
      <div className="py-5 row justify-content-center">
        <div className="col-11 card">
          <table className="table table-response">
            <tbody>
              {data.map((order, index) => {
                return (
                  <tr key={index}>
                    <td>
                      {" "}
                      <img
                        src={assets.delivery}
                        alt=""
                        height={48}
                        width={48}
                      />
                    </td>
                    <td>
                      {order.orderedItems.map((item, index) => {
                        if (index === order.orderedItems.length - 1) {
                          return item.name + " x " + item.quantity;
                        } else {
                          return item.name + " x " + item.quantity + ", ";
                        }
                      })}
                    </td>
                    <td>
                      {" "}
                      {new Intl.NumberFormat("vi-VN", {
                        style: "currency",
                        currency: "VND",
                      }).format(order.amount)}
                    </td>
                    <td>Items: {order.orderedItems.length}</td>
                    <td className="fw-bold text-capitalize">
                      &#x25cf;{order.orderStatus}
                    </td>
                    <td>
                      <button
                        className="btn btn-sm btn-warning"
                        onClick={fetchOrders}
                      >
                        <i className="bi bi-arrow-clockwise"></i>
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
export default MyOrders;
