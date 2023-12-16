import { create } from 'zustand';
import moment from 'moment';
import { getReviewDetail, getReviewStats } from '../services/mypage';
import { useDate } from './stats';
import { useUser } from './user';
import { MoodsComments } from '../constants/StatsComment';

interface Mood {
  name: string;
  icon: string;
  num: number;
}

interface MoodDetail {
  review_id: number;
  subject: string;
  feelings_score: number;
  task_date: string;
}

export interface MoodStore {
  moods: Mood[];
  getMoods: () => void;
  topMoodsComments: { comment: string; subComment: string };
  getTopMoodsComments: () => void;
  topMoods: { firstMood: Mood; secondMood: Mood };
  findTopMoods: () => void;
  moodDetail: MoodDetail[];
  getMoodDetail: ({ subject, page }: { subject: string; page: number }) => void;
}

export const useMoods = create<MoodStore>((set) => ({
  moods: [
    { name: 'HAPPY', icon: '🥰', num: 0 },
    { name: 'NICE', icon: '😀', num: 0 },
    { name: 'GOOD', icon: '🙂', num: 0 },
    { name: 'SOSO', icon: '😐', num: 0 },
    { name: 'SAD', icon: '🙁', num: 0 },
    { name: 'CRY', icon: '😢', num: 0 },
  ],
  getMoods: async () => {
    const { year, month } = useDate.getState();
    const { userId } = useUser.getState();

    const firstDay = moment(new Date(year, month - 1, 1)).format('YYYY-MM-DD');
    const lastDay = moment(new Date(year, month, 0)).format('YYYY-MM-DD');

    const userReviewStats = {
      userId: userId.toString(),
      startDate: firstDay,
      endDate: lastDay,
    };

    const feelings = await getReviewStats(userReviewStats);
    set((state) => {
      const newMoods = [...state.moods];
      newMoods[5].num = feelings['1'];
      newMoods[4].num = feelings['2'] + feelings['3'];
      newMoods[3].num = feelings['4'] + feelings['5'];
      newMoods[2].num = feelings['6'] + feelings['7'];
      newMoods[1].num = feelings['8'] + feelings['9'];
      newMoods[0].num = feelings['10'];
      return { moods: newMoods };
    });
  },
  topMoodsComments: {
    comment: '',
    subComment: '',
  },
  getTopMoodsComments: () => {
    set((state) => {
      const topMoods = { ...state.topMoods };

      const firstMood = MoodsComments.find(
        (moods) => moods.name === topMoods.firstMood.name,
      )?.comment;

      const secondMood = MoodsComments.find(
        (moods) => moods.name === topMoods.secondMood.name,
      )?.comment;

      let comment = '';
      let subComment = '';

      if (firstMood && secondMood) {
        comment = `${firstMood}이 가장 많았어요`;
        subComment = `두번째로는 ${secondMood}이 많았어요`;
      } else if (firstMood && !secondMood) {
        comment = `${firstMood}이 가장 많았어요`;
        subComment = '그 외 감정은 없어요';
      } else {
        comment = '기록된 감정이 없어요';
        subComment = '회고를 작성해 보는 건 어떨까요?';
      }

      return {
        topMoodsComments: {
          comment: comment,
          subComment: subComment,
        },
      };
    });
  },
  topMoods: {
    firstMood: { name: '', icon: '', num: 0 },
    secondMood: { name: '', icon: '', num: 0 },
  },
  findTopMoods: () => {
    set((state) => {
      const newMoods = [...state.moods];
      newMoods.sort((a, b) => b.num - a.num);
      const newFirstMood =
        newMoods[0].num === 0 ? { name: '', icon: '', num: 0 } : newMoods[0];
      const newSecondMood =
        newMoods[1].num === 0 ? { name: '', icon: '', num: 0 } : newMoods[1];

      return {
        topMoods: {
          firstMood: newFirstMood,
          secondMood: newSecondMood,
        },
      };
    });
  },
  moodDetail: [],
  getMoodDetail: async ({ subject, page }) => {
    const { year, month } = useDate.getState();
    const { userId } = useUser.getState();

    const firstDay = moment(new Date(year, month - 1, 1)).format('YYYY-MM-DD');
    const lastDay = moment(new Date(year, month, 0)).format('YYYY-MM-DD');

    let feelingsScore = [0];

    switch (subject) {
      case 'HAPPY':
        feelingsScore = [10];
        break;
      case 'NICE':
        feelingsScore = [9, 8];
        break;
      case 'GOOD':
        feelingsScore = [7, 6];
        break;
      case 'SOSO':
        feelingsScore = [5, 4];
        break;
      case 'SAD':
        feelingsScore = [3, 2];
        break;
      case 'CRY':
        feelingsScore = [1];
        break;
    }

    const userReview = {
      userId: userId.toString(),
      feelingsScore: feelingsScore,
      startDate: firstDay,
      endDate: lastDay,
      page: page,
    };

    const detailContent = await getReviewDetail(userReview);
    set(() => {
      return { moodDetail: detailContent };
    });
  },
}));
