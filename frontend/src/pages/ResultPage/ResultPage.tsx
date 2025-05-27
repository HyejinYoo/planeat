import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import api from '../../api/axios';
import styles from './ResultPage.module.css';

interface RecommendationItem {
  keyword: string;
  placeName: string;
  address: string;
  category: string;
}

function ResultPage() {
  const { code } = useParams<{ code: string }>();
  const [recommendations, setRecommendations] = useState<RecommendationItem[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get(`/api/recommend/${code}`);
        const data = res.data[0]?.recommendations || [];
        setRecommendations(data);
      } catch (e) {
        alert('추천 결과를 불러오는 데 실패했습니다.');
      }
    };

    fetchData();
  }, [code]);

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>🌟 취향에 딱 맞는 장소를 추천드릴게요!</h2>

      {recommendations.length === 0 ? (
        <p className={styles.empty}>추천된 장소가 없습니다.</p>
      ) : (
        <div className={styles.cardList}>
          {recommendations.map((item, index) => (
            <div key={index} className={styles.card}>
              <p className={styles.keyword}>✨ 키워드: {item.keyword}</p>
              <h3 className={styles.placeName}>{item.placeName}</h3>
              <p className={styles.address}>{item.address}</p>
              <p className={styles.category}>📍 {item.category}</p>
              <a
                href={`https://map.kakao.com/?q=${encodeURIComponent(item.placeName)}`}
                target="_blank"
                rel="noopener noreferrer"
                className={styles.kakaoLink}
              >
                카카오맵에서 보기 →
              </a>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default ResultPage;
