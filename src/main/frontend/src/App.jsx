import MainRouter from './MainRouter';
import { BrowserRouter } from 'react-router-dom';
import { CssBaseline, ThemeProvider } from '@mui/material';
import Themes from './themes';

const App = () => {
  return (
    <ThemeProvider theme={Themes.default}>
      <CssBaseline />
      <BrowserRouter>
        <MainRouter />
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
