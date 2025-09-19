import React from "react";
import { Link } from "react-router-dom";
import { assets } from "../../assets/assets";

const SideBar = ({ sidebarVisible }) => {
  const handleLogout = () => {
    localStorage.removeItem("access-token");
    localStorage.removeItem("refresh-token");
    window.location.href = "/login";
  };
  return (
    <div
      className={`d-flex flex-column border-end bg-white ${
        sidebarVisible ? "" : "d-none"
      }`}
      id="sidebar-wrapper"
      style={{ minHeight: "100vh" }} // cho sidebar chiếm full chiều cao màn hình
    >
      <div className="sidebar-heading border-bottom bg-light">
        <img src={assets.logo} alt="" height={48} width={48} />
      </div>

      <div className="list-group list-group-flush">
        <Link
          className="list-group-item list-group-item-action list-group-item-light p-3"
          to="/add"
        >
          <i className="bi bi-plus-circle me-2"></i>
          Add Food
        </Link>
        <Link
          className="list-group-item list-group-item-action list-group-item-light p-3"
          to="/list"
        >
          <i className="bi bi-list-ul me-2"></i>
          List Food
        </Link>
        <Link
          className="list-group-item list-group-item-action list-group-item-light p-3"
          to="/orders"
        >
          <i className="bi bi-cart me-2"></i>
          Orders
        </Link>
      </div>

      {/* nút logout ở cuối */}
      <div className="mt-auto">
        <Link
          className="list-group-item list-group-item-action list-group-item-light p-3"
          to="/login"
          onClick={handleLogout}
        >
          <i className="bi bi-box-arrow-right me-2"></i>
          Logout
        </Link>
      </div>
    </div>
  );
};
export default SideBar;
