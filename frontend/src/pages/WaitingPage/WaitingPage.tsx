import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styles from './WaitingPage.module.css';
import api from '../../api/axios';

function WaitingPage() {
  const { code } = useParams<{ code: string }>();
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const res = await api.post(`/api/recommend/${code}`);
        if (res.status === 200) {
          navigate(`/invite/${code}/result`);
        }
      } catch (err) {
        // 아직 결과가 준비되지 않은 상태면 무시
      }
    }, 3000);
    return () => clearInterval(interval);
  }, [code, navigate]);

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>⏳ 결과를 준비 중이에요!</h2>
      <p className={styles.subtitle}>
        모든 친구들이 응답하면,<br />
        여러분만을 위한 코스를 보여드릴게요 💖
      </p>
      <div className={styles.spinner}></div>
    </div>
  );
}

export default WaitingPage;
