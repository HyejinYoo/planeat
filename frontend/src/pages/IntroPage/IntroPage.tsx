import styles from './IntroPage.module.css';
import logo from '../../assets/planeat-logo.png'; 

function IntroPage() {
  return (
    <div className={styles.introContainer}>
      <img src={logo} alt="Planeat 로고" className={styles.logoImage} />
      <p className={styles.slogan}>
        함께 정하는 취향 맞춤 맛집<br />
        <span className={styles.sloganSub}>같이 가고 싶은 곳, 함께 정해요</span>
      </p>
      <a className={styles.introLink} href="/create">
        초대 생성하기
      </a>
    </div>
  );
}

export default IntroPage;