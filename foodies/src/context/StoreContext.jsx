import { createContext, useEffect, useState } from "react";
import { getAllFood } from "../services/FoodService";
import { toast } from "react-toastify";
import axios from "axios";
import {
  addToCart,
  getCartData,
  removeItemFromCart,
  removeQuantityFromCart,
} from "../services/cartService";

export const StoreContext = createContext(null);
export const StoreContextProvider = (props) => {
  const [foodList, setFoodList] = useState([]);
  const [pageIndex, setPageIndex] = useState(1);
  const [quantities, setQuantities] = useState({});
  const [token, setToken] = useState("");

  const increaseQty = async (foodId) => {
    setQuantities((prev) => ({ ...prev, [foodId]: (prev[foodId] || 0) + 1 }));
    await addToCart(foodId, token);
  };

  const decreaseQty = async (foodId) => {
    setQuantities((prev) => ({
      ...prev,
      [foodId]: prev[foodId] > 0 ? prev[foodId] - 1 : 0,
    }));
    await removeQuantityFromCart(foodId, token);
  };
  const fetchFoodList = async () => {
    try {
      const result = await getAllFood(pageIndex, 6);
      console.log(result);
      setFoodList(result.data.data.data);
    } catch (error) {
      toast.error("Error while reading the foods.");
    }
  };

  const removeFromCart = (foodId) => {
    setQuantities((prevQuantities) => {
      const updateQuantites = { ...prevQuantities };
      delete updateQuantites[foodId];
      return updateQuantites;
    });
    removeItemFromCart(foodId, token);
  };

  const loadCartData = async (token) => {
    const response = await getCartData(token);
    setQuantities(response.data.data.items);
  };
  const contextMenu = {
    foodList,
    increaseQty,
    decreaseQty,
    quantities,
    removeFromCart,
    token,
    setToken,
    setQuantities,
    loadCartData,
  };
  useEffect(() => {
    async function loadData() {
      await fetchFoodList();
      if (localStorage.getItem("access-token")) {
        setToken(localStorage.getItem("access-token"));
        await loadCartData(localStorage.getItem("access-token"));
      }
    }
    loadData();
  }, []);
  return (
    <StoreContext.Provider value={contextMenu}>
      {props.children}
    </StoreContext.Provider>
  );
};
