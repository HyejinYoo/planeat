import { useState } from 'react';
import api from '../../api/axios';
import styles from './CreateInvitePage.module.css';
import logo from '../../assets/sub-image.png'; // 경로는 실제 위치에 맞게 조정


function CreateInvitePage() {
  const [participantCount, setParticipantCount] = useState<number>(2);
  const [steps, setSteps] = useState<string[]>([]);
  const [inviteCode, setInviteCode] = useState<string>('');

  const stepOptions: string[] = ['MEAL', 'DESSERT', 'ACTIVITY'];

  const toggleStep = (step: string) => {
    setSteps((prev) =>
      prev.includes(step) ? prev.filter((s) => s !== step) : [...prev, step]
    ); 
  };  

  const handleCreate = async () => {
    try {
      const res = await api.post('/api/invite', {
        participantCount,
        steps,
      });
      setInviteCode(res.data.inviteCode);
    } catch (e) {
      alert('초대 생성 실패!');
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>🎉 함께할 여정을 시작해볼까요?</h2>

      <img src={logo} alt="Planeat 로고" className={styles.logoImage} />

      {inviteCode && (
        <div className={styles.inviteCodeBox}>
          <p>✅ <b>초대 코드:</b> {inviteCode}</p>
          <p>🔗 <a href={`/invite/${inviteCode}`}>/invite/{inviteCode}</a></p>
        </div>
      )}

      <div className={styles.section}>
        <p className={styles.sectionGuide}>👥 참여 인원 수를 알려주세요</p>
        <input
          type="number"
          value={participantCount}
          min={1}
          onChange={(e) => setParticipantCount(Number(e.target.value))}
          className={styles.input}
        />
      </div>

      <div className={styles.section}>
        <p className={styles.sectionGuide}>💡 어떤 코스를 함께 하고 싶으신가요?</p>
        <div className={styles.stepButtonGroup}>
          {stepOptions.map((step) => (
            <button
              key={step}
              type="button"
              className={`${styles.stepButton} ${steps.includes(step) ? styles.active : ''}`}
              onClick={() => toggleStep(step)}
            >
              {step === 'MEAL' && '🍽️ 식사'}
              {step === 'DESSERT' && '🍰 디저트'}
              {step === 'ACTIVITY' && '🎮 활동'}
            </button>
          ))}
        </div>
      </div>

      <button onClick={handleCreate} className={styles.submitButton}>
        🚀 초대 코드 만들기
      </button>
    </div>
  );
}

export default CreateInvitePage;
