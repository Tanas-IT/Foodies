import React, { useContext, useState } from "react";
import "./Login.css";
import { Link, useNavigate } from "react-router-dom";
import { login } from "./AuthenService";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode";
const Login = () => {
  const navigate = useNavigate();
  const [token, setToken] = useState("");
  const [data, setData] = useState({
    email: "",
    password: "",
  });

  const onChangeHandler = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setData((data) => ({ ...data, [name]: value }));
  };

  const onSubmitHandler = async (event) => {
    event.preventDefault();
    try {
      const result = await login(data);
      console.log(result.data.data.accessToken);
      if (result.status === 200) {
        setToken(result.data.data.accessToken);
        localStorage.setItem("access-token", result.data.data.accessToken);
        localStorage.setItem("refresh-token", result.data.data.refreshToken);
        const decoded = jwtDecode(result.data.data.refreshToken);
        const role =
          decoded.role ||
          decoded[
            "http://schemas.microsoft.com/ws/2008/06/identity/claims/role"
          ];
        if (role.toLowerCase() !== "admin") {
          toast.error("You do not have permit to login admin panel");
        } else {
          navigate("/");
        }
      } else {
        toast.error("Unable to login. Please try again");
      }
    } catch (error) {
      console.log("Error while login", error);
      toast.error("Unable to login. Please try again");
    }
  };
  return (
    <div className="login-container">
      <div className="row">
        <div className="col-sm-9 col-md-7 col-lg-5 mx-auto">
          <div className="card border-0 shadow rounded-3 my-5">
            <div className="card-body p-4 p-sm-5">
              <h5 className="card-title text-center mb-5 fw-light fs-5">
                Sign In
              </h5>
              <form onSubmit={onSubmitHandler}>
                <div className="form-floating mb-3">
                  <input
                    type="email"
                    className="form-control"
                    id="floatingInput"
                    placeholder="name@example.com"
                    name="email"
                    onChange={onChangeHandler}
                    value={data.email}
                  />
                  <label htmlFor="floatingInput">Email address</label>
                </div>
                <div className="form-floating mb-3">
                  <input
                    type="password"
                    className="form-control"
                    id="floatingPassword"
                    placeholder="Password"
                    name="password"
                    onChange={onChangeHandler}
                    value={data.password}
                  />
                  <label htmlFor="floatingPassword">Password</label>
                </div>

                <div className="d-grid">
                  <button
                    className="btn btn-outline-primary btn-login text-uppercase"
                    type="submit"
                  >
                    Sign in
                  </button>
                  <button
                    className="btn btn-outline-danger btn-login text-uppercase mt-2"
                    type="reset"
                  >
                    Reset
                  </button>
                </div>
                <div className="mt-4">
                  Don't have an account? <Link to="/register">Sign up</Link>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
