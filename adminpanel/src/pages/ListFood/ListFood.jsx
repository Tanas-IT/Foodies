import React, { useEffect, useState } from "react";
import { deleteFood, getListFood } from "../../services/FoodService";
import { toast } from "react-toastify";
import "./ListFood.css";

const ListFood = () => {
  const [list, setList] = useState([]);
  const [page, setPage] = useState([]);
  const [pageIndex, setPageIndex] = useState(1);

  const fetchList = async (pageIndex) => {
    try {
      const response = await getListFood(pageIndex);
      console.log(response);
      if (response.status === 200) {
        console.log(response.data.data);
        setPage(response.data.data);
        setList(response.data.data.data);
      } else {
        toast.error("Error while reading the foods.");
      }
    } catch (error) {
      toast.error("Error while reading the foods.");
    }
  };
  useEffect(() => {
    fetchList(pageIndex);
  }, []);

  const removeFood = async (foodId) => {
    try {
      const data = await deleteFood(foodId);
      if (data.status === 200) {
        toast.success("Food remove");
        await fetchList(1);
      } else {
        toast.error("Error occured while removing the food.");
      }
    } catch (error) {
      toast.error("Error occured while removing the food.");
    }
  };

  const handlePagination = (pageIndex) => {
    if (pageIndex > 0 && pageIndex <= page.totalPages) {
      setPageIndex(pageIndex);
      fetchList(pageIndex);
    }
  };
  return (
    <>
      <div className="py-5 row justify-content-center">
        <div className="col-11 card">
          <table className="table">
            <thead>
              <tr>
                <th>Image</th>
                <th>Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {list.map((item, index) => {
                return (
                  <tr key={index}>
                    <td>
                      <img
                        src={item.imageURL}
                        alt=""
                        height={48}
                        width={48}
                        style={{ objectFit: "cover" }}
                      />
                    </td>
                    <td>{item.name}</td>
                    <td>{item.category}</td>
                    <td>&#8363;{item.price}</td>
                    <td className="text-danger">
                      <i
                        className="bi bi-x-circle-fill"
                        onClick={() => removeFood(item.id)}
                      ></i>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
      <nav className="d-flex py-5 justify-content-center">
        <ul className="pagination">
          <li className="page-item">
            <span
              to="#"
              className="page-link"
              onClick={() => handlePagination(pageIndex - 1)}
            >
              Previous
            </span>
          </li>
          {Array.from({ length: page.totalPages }, (_, index) => (
            <li
              key={index}
              className={`page-item ${pageIndex === ++index ? "active" : ""}`}
            >
              <span
                className="page-link"
                onClick={() => handlePagination(index)}
              >
                {index}
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
    </>
  );
};
export default ListFood;
