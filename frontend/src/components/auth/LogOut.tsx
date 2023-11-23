import { useContext, useEffect } from "react";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

export function Logout() {
  const navigate = useNavigate();
  const authContext = useContext(AuthContext);
  authContext.logout();

  useEffect(() => {
    navigate("/login");
  }, [navigate]);
  return <div></div>;
}
