import { useEffect, useState } from 'react';
import { MapContainer, Marker, TileLayer, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import type { LatLngExpression } from 'leaflet';
import { icon } from 'leaflet';
import axios from 'axios';
import api from '../../api/axios';
import { useNavigate, useParams } from 'react-router-dom';
import styles from './JoinPage.module.css';

const DefaultIcon = icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

const keywordOptions: Record<string, string[]> = {
  MEAL: ['한식', '중식', '양식', '일식', '분식', '고기구이', '족발/보쌈', '치킨/피자', '회', '패스트푸드'],
  DESSERT: ['카페', '케이크', '빙수', '베이커리', '아이스크림'],
  ACTIVITY: ['노래방', '방탈출', '영화관', '산책', '게임', '보드카페'],
};

function LocationMarker({ setLatLng }: { setLatLng: (lat: number, lng: number) => void }) {
  useMapEvents({
    click(e) {
      setLatLng(e.latlng.lat, e.latlng.lng);
    },
  });
  return null;
}

function JoinPage() {
  const { code } = useParams<{ code: string }>();
  const navigate = useNavigate();

  const [name, setName] = useState('');
  const [latitude, setLatitude] = useState<number | null>(null);
  const [longitude, setLongitude] = useState<number | null>(null);
  const [address, setAddress] = useState('');
  const [activeSteps, setActiveSteps] = useState<string[]>([]);
  const [stepKeywords, setStepKeywords] = useState<Record<string, string[]>>({});
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState<any[]>([]);

  useEffect(() => {
    const fetchInvite = async () => {
      try {
        const res = await api.get(`/api/invite/${code}`);
        const stepsFromServer: string[] = res.data.steps;
        setActiveSteps(stepsFromServer);

        const keywordObj: Record<string, string[]> = {};
        stepsFromServer.forEach((step) => {
          keywordObj[step] = [];
        });
        setStepKeywords(keywordObj);
      } catch (e) {
        alert('초대 정보 불러오기 실패');
      }
    };
    fetchInvite();
  }, [code]);

  const fetchAddressFromCoords = async (lat: number, lng: number) => {
    try {
      const res = await axios.get('https://dapi.kakao.com/v2/local/geo/coord2address.json', {
        params: { x: lng, y: lat },
        headers: {
          Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
        },
      });
      const result = res.data.documents[0];
      if (result?.road_address) setAddress(result.road_address.address_name);
      else if (result?.address) setAddress(result.address.address_name);
      else setAddress('주소를 찾을 수 없습니다.');
    } catch {
      setAddress('주소 변환 실패');
    }
  };

  const setLatLon = (lat: number, lon: number) => {
    setLatitude(lat);
    setLongitude(lon);
    fetchAddressFromCoords(lat, lon);
  };

  const getLocation = () => {
    navigator.geolocation.getCurrentPosition(
      (pos) => setLatLon(pos.coords.latitude, pos.coords.longitude),
      () => alert('위치 권한이 필요합니다.')
    );
  };

  const handleSearch = async () => {
    try {
      const res = await axios.get('https://dapi.kakao.com/v2/local/search/keyword.json', {
        params: { query: searchQuery },
        headers: {
          Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
        },
      });
      setSearchResults(res.data.documents);
    } catch {
      alert('검색 실패');
    }
  };

  const handleKeywordToggle = (step: string, keyword: string) => {
    setStepKeywords((prev) => {
      const current = prev[step] || [];
      const updated = current.includes(keyword)
        ? current.filter((k) => k !== keyword)
        : current.length < 3
        ? [...current, keyword]
        : current;
      return { ...prev, [step]: updated };
    });
  };

  const handleSubmit = async () => {
    if (!name || latitude == null || longitude == null) {
      alert('이름과 위치를 입력해주세요.');
      return;
    }

    const preferences = activeSteps.map((step) => ({
      stepType: step,
      keyword1: stepKeywords[step]?.[0] || '',
      keyword2: stepKeywords[step]?.[1] || '',
      keyword3: stepKeywords[step]?.[2] || '',
    }));

    try {
      await api.post(`/api/invite/${code}/respond`, {
        name,
        preferences,
        latitude,
        longitude,
      });
      navigate(`/invite/${code}/wait`);
    } catch (err) {
      alert('응답 제출 실패');
      console.error(err);
    }
  };

  return (
    <div className={styles.container}>
      <p className={styles.sectionGuide}>✏️ 당신의 이름을 알려주세요</p>
      <div className={styles.inputWrapper}>
        <input
          type="text"
          placeholder="이름"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className={styles.input}
        />
      </div>

      <p className={styles.sectionGuide}>📍 어디서 출발하실 예정인가요?</p>
      <div className={styles.searchRow}>
        <input
          type="text"
          placeholder="장소 검색 (예: 이대역)"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className={styles.input}
        />
        <button onClick={handleSearch} className={styles.button}>검색</button>
      </div>

      <div className={`${styles.mapSearchLayout} ${searchResults.length > 0 ? styles.withResults : styles.fullWidth}`}>
        <div className={styles.mapBox}>
          <MapContainer
            center={[latitude || 37.5665, longitude || 126.9780] as LatLngExpression}
            zoom={13}
            scrollWheelZoom={true}
            style={{ height: '300px', width: '100%' }}
          >
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
            {latitude && longitude && (
              <Marker position={[latitude, longitude]} icon={DefaultIcon} />
            )}
            <LocationMarker setLatLng={setLatLon} />
          </MapContainer>
        </div>

        {searchResults.length > 0 && (
          <div className={styles.resultBox}>
            {searchResults.map((place) => (
              <div
                key={place.id}
                className={styles.resultItem}
                onClick={() => setLatLon(Number(place.y), Number(place.x))}
              >
                📍 {place.place_name}
              </div>
            ))}
          </div>
        )}
      </div>


      {address && <p className={styles.addressText}>나의 위치: <strong>{address}</strong></p>}

      <div className={styles.inputGroup}>
        <button onClick={getLocation} className={styles.locationButton}>
          현재 위치 불러오기
        </button>
      </div>

      <p className={styles.sectionGuide}>🧸 이런 곳 어때요? 마음에 드는 걸 골라보세요!</p>
      {activeSteps.map((step) => {
        let guide = '';
        if (step === 'MEAL') guide = '🍽️ 먹고 싶은 메뉴를 골라주세요!';
        else if (step === 'DESSERT') guide = '🍰 디저트는 어떤 게 좋을까요?';
        else if (step === 'ACTIVITY') guide = '🎮 함께 하고 싶은 활동은 무엇인가요?';

        return (
          <div key={step} className={styles.stepSection}>
            <h4 className={styles.stepTitle}>{guide} (최대 3개)</h4>
            <div className={styles.keywordButtonGroup}>
              {keywordOptions[step]?.map((keyword) => {
                const selected = stepKeywords[step]?.includes(keyword);
                return (
                  <button
                    key={keyword}
                    type="button"
                    className={`${styles.keywordButton} ${selected ? styles.selected : ''}`}
                    onClick={() => handleKeywordToggle(step, keyword)}
                  >
                    {keyword}
                  </button>
                );
              })}
            </div>
          </div>
        );
      })}

      <button onClick={handleSubmit} className={styles.submitButton}>
        함께하러 가기
      </button>
    </div>
  );
}

export default JoinPage;