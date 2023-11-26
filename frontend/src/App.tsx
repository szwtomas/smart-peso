import { BrowserRouter } from "react-router-dom";
import { Providers } from "./Providers";
import { AppRoutes } from "./AppRoutes";

const App = () => {
  return (
    <>
      <Providers>
        <BrowserRouter>
          <AppRoutes />
        </BrowserRouter>
      </Providers>
    </>
  );
};

export default App;
