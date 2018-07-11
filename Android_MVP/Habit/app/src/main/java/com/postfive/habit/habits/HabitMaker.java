package com.postfive.habit.habits;

import com.postfive.habit.habits.factory.Habit;
import com.postfive.habit.habits.factory.HabitFactory;
import com.postfive.habit.habits.storage.DrinkWaterHabit;
import com.postfive.habit.habits.storage.PreStudyHabit;
import com.postfive.habit.habits.storage.SkipRopeHabit;
import com.postfive.habit.habits.storage.UserSetHabit;

public class HabitMaker extends HabitFactory {
    @Override
    public Habit createHabit(int habitId) {
/*        if (habitId == null){
            return null;
        }

        if(habit.equals("drinkwater")){
            return new DrinkWaterHabit();
        }else if (habit.equals("prestudy")){
            return new PreStudyHabit();
        }else if (habit.equals("skiprope")){
            return new SkipRopeHabit();
        }*/

//        switch (habitId){
//            case 1:
//                return new DrinkWaterHabit();
//
//            case 2:
//                return new SkipRopeHabit();
//            case 3 :
//                return new PreStudyHabit();
//            default :
//                break;
//        }
        if(habitId == 1){
            return new DrinkWaterHabit();
        }else if (habitId == 2){
            return new SkipRopeHabit();
        }else if (habitId == 0){
            return new UserSetHabit();
        }

        return null;
    }


    @Override
    public Habit createHabit(String habitType) {
        if (habitType == null){
            return null;
        }

        if(habitType.equals("drinkwater")){
            return new DrinkWaterHabit();
        }else if (habitType.equals("prestudy")){
            return new PreStudyHabit();
        }else if (habitType.equals("skiprope")){
            return new SkipRopeHabit();
        }
        return null;
    }


    public Class getHabit(String habitType) {
        if (habitType == null){
            return null;
        }

        if(habitType.equals("drinkwater")){
            return DrinkWaterHabit.class;
        }else if (habitType.equals("prestudy")){
            return PreStudyHabit.class;
        }else if (habitType.equals("skiprope")){
            return SkipRopeHabit.class;
        }
        return null;
    }

    class EmptyHabit extends Habit{
        @Override
        public void prepare() {

        }
    }

}