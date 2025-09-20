import React, { useEffect, useState } from "react";
import FoodItem from "../FoodItem/FoodItem";
import { getAllFood } from "../../services/FoodService";
import { toast } from "react-toastify";

const FoodDisplay = ({ category, searchText }) => {
  const [page, setPage] = useState({ data: [], totalPages: 0 });
  const [pageIndex, setPageIndex] = useState(1);
  const [loading, setLoading] = useState(false);
  const [allFoods, setAllFoods] = useState([]);

  const pageSize = 6;

  // API phân trang
  const fetchList = async (pageIndex) => {
    try {
      setLoading(true);
      const res = await getAllFood(pageIndex, pageSize);
      if (res.status === 200) {
        const data = res.data.data;
        setPage({
          data: data.data,
          totalPages: data.totalPages,
        });
      }
    } catch {
      toast.error("Error while fetching foods");
    } finally {
      setLoading(false);
    }
  };

  // API lấy tất cả foods để filter
  const fetchAllFoods = async () => {
    try {
      const res = await getAllFood(1, 1000); // load nhiều item
      if (res.status === 200) {
        setAllFoods(res.data.data.data);
      }
    } catch {
      toast.error("Error fetching all foods");
    }
  };

  // load phân trang khi không filter
  useEffect(() => {
    if (!searchText && category === "All") {
      fetchList(pageIndex);
    }
  }, [pageIndex, searchText, category]);

  // load all foods khi filter
  useEffect(() => {
    if (searchText || category !== "All") {
      if (allFoods.length === 0) {
        fetchAllFoods();
      }
      setPageIndex(1); // reset về page đầu
    }
  }, [searchText, category]);

  // chọn dữ liệu hiển thị
  let filteredFoods = [];
  let totalPages = page.totalPages;

  if (searchText || category !== "All") {
    const filtered = allFoods.filter(
      (food) =>
        (category === "All" ||
          food.category.toLowerCase() === category.toLowerCase()) &&
        food.name.toLowerCase().includes(searchText.toLowerCase())
    );
    totalPages = Math.ceil(filtered.length / pageSize);
    const start = (pageIndex - 1) * pageSize;
    const end = start + pageSize;
    filteredFoods = filtered.slice(start, end);
  } else {
    filteredFoods = page.data;
  }

  const handlePagination = (newPageIndex) => {
    if (newPageIndex > 0 && newPageIndex <= totalPages) {
      setPageIndex(newPageIndex);
      if (!(searchText || category !== "All")) {
        fetchList(newPageIndex);
      }
    }
  };

  return (
    <div className="container-fluid">
      <div className="row">
        {loading ? (
          <div className="text-center py-5">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : filteredFoods.length > 0 ? (
          filteredFoods.map((food, index) => (
            <FoodItem
              key={index}
              name={food.name}
              description={food.description}
              id={food.id}
              imageURL={food.imageURL}
              price={food.price}
            />
          ))
        ) : (
          <div className="text-center mt-4">
            <h4>No food found</h4>
          </div>
        )}
      </div>

      {!loading && filteredFoods.length > 0 && (
        <nav className="d-flex py-5 justify-content-center">
          <ul className="pagination">
            <li className="page-item">
              <span
                className="page-link"
                onClick={() => handlePagination(pageIndex - 1)}
              >
                Previous
              </span>
            </li>
            {Array.from({ length: totalPages }, (_, i) => (
              <li
                key={i + 1}
                className={`page-item ${pageIndex === i + 1 ? "active" : ""}`}
              >
                <span
                  className="page-link"
                  onClick={() => handlePagination(i + 1)}
                >
                  {i + 1}
                </span>
              </li>
            ))}
            <li className="page-item">
              <span
                className="page-link"
                onClick={() => handlePagination(pageIndex + 1)}
              >
                Next
              </span>
            </li>
          </ul>
        </nav>
      )}
    </div>
  );
};

export default FoodDisplay;
