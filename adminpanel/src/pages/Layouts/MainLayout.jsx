import React, { useState } from "react";
import SideBar from "../../components/Sidebar/Sidebar";
import MenuBar from "../../components/Menubar/Menubar";
import { Route, Routes } from "react-router-dom";
import AddFood from "../AddFood/AddFood";
import ListFood from "../ListFood/ListFood";
import Orders from "../Orders/Orders";

const MainLayout = () => {
  const [sidebarVisible, setSidebarVisible] = useState(true);

  const toggleSidebar = () => {
    setSidebarVisible(!sidebarVisible);
  };
  return (
    <div className="d-flex" id="wrapper">
      <SideBar sidebarVisible={sidebarVisible} />
      <div id="page-content-wrapper">
        <MenuBar toggleSidebar={toggleSidebar} />
        <div className="container-fluid">
          <Routes>
            <Route path="/add" element={<AddFood />} />
            <Route path="/list" element={<ListFood />} />
            <Route path="/orders" element={<Orders />} />
          </Routes>
        </div>
      </div>
    </div>
  );
};
export default MainLayout;
