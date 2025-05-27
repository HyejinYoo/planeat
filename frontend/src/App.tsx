import { Routes, Route } from 'react-router-dom';
import IntroPage from './pages/IntroPage/IntroPage.tsx';
import CreateInvitePage from './pages/CreateInvitePage/CreateInvitePage.tsx';
import JoinPage from './pages/JoinPage/JoinPage.tsx';
import WaitingPage from './pages/WaitingPage/WaitingPage.tsx';
import ResultPage from './pages/ResultPage/ResultPage.tsx';

function App() {
  return (
    <Routes>
      <Route path="/" element={<IntroPage />} />
      <Route path="/create" element={<CreateInvitePage />} />
      <Route path="/invite/:code" element={<JoinPage />} />
      <Route path="/invite/:code/wait" element={<WaitingPage />} />
      <Route path="/invite/:code/result" element={<ResultPage />} />
    </Routes>
  );
}

export default App;