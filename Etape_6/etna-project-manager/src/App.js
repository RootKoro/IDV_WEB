import { Routes, Route} from 'react-router-dom';

import { Address } from './pages/address';
import { Auth } from './pages/auth';
import { Dashboard } from './pages/dashboard';
import { Profile } from './pages/profile';
import './styles/App.css';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Auth />} />
      <Route path="/home" element={<Dashboard />} />
      <Route path='/addresses' element={<Address />} />
      <Route path='/profile' element={<Profile />} />
    </Routes>
  );
}

export default App;
