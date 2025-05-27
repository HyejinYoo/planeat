import { useEffect } from 'react';

declare global {
  interface Window {
    kakao: any;
  }
}

interface MarkerData {
  placeName: string;
  address: string;
}

interface KakaoMapProps {
  markers: MarkerData[];
}

function KakaoMap({ markers }: KakaoMapProps) {
  useEffect(() => {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY}&autoload=false`;
    script.async = true;

    script.onload = () => {
      window.kakao.maps.load(() => {
        const container = document.getElementById('map');
        const options = {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780),
          level: 5,
        };
        const map = new window.kakao.maps.Map(container, options);

        markers.forEach((marker) => {
          const geocoder = new window.kakao.maps.services.Geocoder();
          geocoder.addressSearch(marker.address, (result: any, status: any) => {
            if (status === window.kakao.maps.services.Status.OK) {
              const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
              const kakaoMarker = new window.kakao.maps.Marker({
                map,
                position: coords,
              });

              const infoWindow = new window.kakao.maps.InfoWindow({
                content: `<div style="padding:5px;font-size:0.9rem;">${marker.placeName}</div>`,
              });

              window.kakao.maps.event.addListener(kakaoMarker, 'click', () => {
                infoWindow.open(map, kakaoMarker);
                map.panTo(coords);
              });
            }
          });
        });
      });
    };

    document.head.appendChild(script);
  }, [markers]);

  return <div id="map" style={{ width: '100%', height: '100%' }}></div>;
}

export default KakaoMap;
