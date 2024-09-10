package fueldispense;


import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class FuelDispense {

    public static int solution(int[] a, int x, int y, int z) {
        List<CarStatus> carStatuses = generateCarStatusList(a);
        int time = 0;
        while (!isCarFinished(carStatuses)) {
            log.println("at time " + time);
            for (CarStatus carStatus : carStatuses) {
                boolean isFuelLeveXSufficient = isFuelLevelSufficient(x, carStatus);
                boolean isDispenserXAvailable = findIfDispenserAvailable(carStatuses, CarStatus.Dispenser.X, carStatus, time);
                boolean allowForDispenserX = checkAllowForDispenserX(isFuelLeveXSufficient, isDispenserXAvailable);
                if (allowForDispenserX) {
                    updateWaitingTime(carStatus, time, CarStatus.Dispenser.X);
                    x--;
                    carStatus.fuelNeeded--;
                    checkFuel(carStatus, time);
                    logMessage(carStatus, x);
                } else if (y > 0 && y > carStatus.fuelNeeded && findIfDispenserAvailable(carStatuses, CarStatus.Dispenser.Y, carStatus, time)) {
                    updateWaitingTime(carStatus, time, CarStatus.Dispenser.Y);
                    y--;
                    carStatus.fuelNeeded--;
                    checkFuel(carStatus, time);
                    logMessage(carStatus, y);
                } else if (z > 0 && z > carStatus.fuelNeeded && findIfDispenserAvailable(carStatuses, CarStatus.Dispenser.Z, carStatus, time)) {
                    updateWaitingTime(carStatus, time, CarStatus.Dispenser.Z);
                    z--;
                    carStatus.fuelNeeded--;
                    checkFuel(carStatus, time);
                    logMessage(carStatus, z);
                }
            }

            time++;
        }
        return findMaxWaitTime(carStatuses);
    }

    private static boolean checkAllowForDispenserX(boolean isFuelLeveXSufficient, boolean isDispenserXAvailable) {
        return isFuelLeveXSufficient && isDispenserXAvailable;
    }


    public static boolean isFuelLevelSufficient(int x, CarStatus carStatus) {
        return x > 0 && x > carStatus.fuelNeeded;
    }

    public static void logMessage(CarStatus carStatus, int dispenserRemainingFuel) {
        String message = generateMessage(carStatus, dispenserRemainingFuel);
        if (carStatus.getFuelNeeded() == 0) {
            message = updateMessage(message, carStatus.carNo);
        }
        log.println(message);
    }

    public static List<CarStatus> generateCarStatusList(int[] a) {
        List<CarStatus> carStatuses = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            carStatuses.add(new CarStatus(i, a[i]));
        }
        return carStatuses;
    }

    public static void updateWaitingTime(CarStatus carStatus, int time, CarStatus.Dispenser dispenser) {
        if (carStatus.waitingTime == null) {
            carStatus.setWaitingTime(time);
            carStatus.setDispenser(dispenser);
            carStatus.setStatus(CarStatus.Status.FILLING);
        }
    }

    public static void checkFuel(CarStatus carStatus, int time) {
        if (carStatus.fuelNeeded == 0) {
            carStatus.status = CarStatus.Status.DONE;
            carStatus.setFinishTime(time);
        }
    }


    public static boolean findIfDispenserAvailable(List<CarStatus> carStatuses, CarStatus.Dispenser dispenser, CarStatus carStatus, int time) {
        boolean isAvailable = false;
        if (carStatus.getDispenser() != null && carStatus.getDispenser() != dispenser) {
            return false;
        }
        for (CarStatus temp : carStatuses) {
            if (temp.getDispenser() != null && temp.getDispenser().equals(dispenser) && temp.status.equals(CarStatus.Status.FILLING)) {
                if (carStatus.carNo == temp.getCarNo() && carStatus.getDispenser().equals(dispenser)) {
                    isAvailable = true;
                    break;
                } else {
                    isAvailable = false;
                    break;
                }
            } else if (temp.getDispenser() == null && temp.status.equals(CarStatus.Status.READY)) {
                if (carStatus.carNo == temp.getCarNo()) {
                    isAvailable = true;
                    break;
                } else {
                    isAvailable = false;
                }
            } else if (temp.getDispenser() != null && temp.status.equals(CarStatus.Status.DONE)) {
                if (carStatus.carNo == temp.getCarNo()) {
                    isAvailable = false;
                    break;
                } else {
                    if (temp.getFinishTime() == time && temp.getDispenser().equals(dispenser)) {
                        isAvailable = false;
                        break;
                    } else {
                        isAvailable = true;
                    }
                }
            }
        }
        return isAvailable;
    }

    public static boolean isCarFinished(List<CarStatus> carStatuses) {
        boolean isDone = false;
        for (CarStatus carStatus : carStatuses) {
            if (carStatus.status != null && carStatus.status.equals(CarStatus.Status.DONE)) {
                isDone = true;
            } else {
                isDone = false;
                break;
            }
        }
        return   isDone;
    }

    public static String generateMessage(CarStatus carStatus, int dispenserRemainingFuel) {
        return String.format(
                "Car %d with status %s is now being filled by Dispenser %s. Fuel needed: %d. Remaining dispenser capacity: %d.",
                carStatus.carNo,
                carStatus.getStatus(), // Current status of the car
                carStatus.getDispenser(),
                carStatus.getFuelNeeded(), // Amount of fuel still needed
                dispenserRemainingFuel // Remaining capacity of the dispenser
        );
    }

    public static String updateMessage(String message, int carNo) {
        return String.format("%s Car %d is now Done", message, carNo);

    }

    public static int findMaxWaitTime(List<CarStatus> carStatuses) {
        int max =0;
        for(CarStatus status : carStatuses) {
            if(status.getWaitingTime()>max){
                max = status.getWaitingTime();
            }
        }
        return max;

    }

    public static class CarStatus {
        private int carNo;
        private int fuelNeeded;
        private Dispenser dispenser;
        private Status status;
        private Integer waitingTime;
        private Integer finishTime;

        public Integer getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(Integer finishTime) {
            this.finishTime = finishTime;
        }

        public int getWaitingTime() {
            return waitingTime;
        }

        public void setWaitingTime(int waitingTime) {
            this.waitingTime = waitingTime;
        }

        public CarStatus(int carNo, int fuelNeeded) {
            this.carNo = carNo;
            this.fuelNeeded = fuelNeeded;
            this.status = Status.READY;
        }

        public int getFuelNeeded() {
            return fuelNeeded;
        }

        public int getCarNo() {
            return carNo;
        }

        public void setCarNo(int carNo) {
            this.carNo = carNo;
        }

        public Dispenser getDispenser() {
            return dispenser;
        }

        public void setDispenser(Dispenser dispenser) {
            this.dispenser = dispenser;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public enum Status {
            READY,
            FILLING,
            DONE,
        }

        public enum Dispenser {
            X,
            Y,
            Z
        }
    }
}


class Solution {
    public static void main(String[] args) {
        int x = 20; // Dispenser x
        int y = 20; // Dispenser y
        int z = 20; // Dispenser z

        // Array representing each car's fuel needs
        int[] cars = {3, 4, 5, 2, 6, 2}; // Example with 6 cars
        int maxWaitingTime = FuelDispense.solution(cars, x, y, z);
        log.println("Max waiting time: " + maxWaitingTime);
    }
}