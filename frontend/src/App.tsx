import { Providers } from "./Providers";
import { Layout } from "./components/Layout";

const App = () => {
  return (
    <>
      <Providers>
        <Layout>
          <h1>Holis</h1>
        </Layout>
      </Providers>
    </>
  );
};

export default App;
