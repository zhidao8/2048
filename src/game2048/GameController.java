package game2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GameController {
    private int countOfDigitsOnOneSide;

    private Int[][] numbers;

    private boolean swapped;
    
    public GameController(int countOfDigitsOnOneSide) {
        this.countOfDigitsOnOneSide = countOfDigitsOnOneSide;

        numbers = new Int[countOfDigitsOnOneSide][countOfDigitsOnOneSide];
        for (int i = 0; i < countOfDigitsOnOneSide; i++) {
            for (int j = 0; j < countOfDigitsOnOneSide; j++) {
                numbers[i][j] = new Int();
            }
        }
    }

    void moveUp() {
        for (int column = 0; column < countOfDigitsOnOneSide; column++) {
            Int[] numbersByColumn = getNumbersByColumn(column);

            for (int row = 0; row < countOfDigitsOnOneSide - 1; row++) {
                Int currentNumber = numbersByColumn[row];
                if (currentNumber.isZero()) continue;

                getIfSameIgnoreZeroValue(numbersByColumn, row + 1, currentNumber)
                        .ifPresent(nextSameNumber -> {
                            currentNumber.doubleTheValue();
                            nextSameNumber.setValue(0);
                        });
            }
        }

        alignNumbersToTop();
    }

    private Int[] getNumbersByColumn(int column) {
        Int[] result = new Int[countOfDigitsOnOneSide];
        for (int i = 0; i < result.length; i++) {
            result[i] = numbers[i][column];
        }
        return result;
    }

    private Optional<Int> getIfSameIgnoreZeroValue(Int[] numbers, int startIndex, Int toFind) {
        for (int i = startIndex; i < numbers.length; i++) {
            if (numbers[i].isZero()) continue;
            if (numbers[i].valueEquals(toFind)) {
                return Optional.of(numbers[i]);
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private void alignNumbersToTop() {
        for (int column = 0; column < countOfDigitsOnOneSide; column++) {
            for (int row = 0; row < countOfDigitsOnOneSide; row++) {
                if (!numbers[row][column].isZero()) continue;

                Optional<Integer> notZeroIndex = findNotZeroIndexByColumn(column, row + 1);
                if (notZeroIndex.isPresent()) {
                    swapTwoNumber(row, column, notZeroIndex.get(), column);
                    swapped = true;
                }
            }
        }
    }

    private Optional<Integer> findNotZeroIndexByColumn(int column, int startIndex) {
        for (int row = startIndex; row < countOfDigitsOnOneSide; row++) {
            if (!numbers[row][column].isZero()) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findNotZeroIndexByReverseColumn(int column, int startIndex) {
        for (int row = startIndex; row >= 0; row--) {
            if (!numbers[row][column].isZero()) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findNotZeroIndexByRow(int row, int startIndex) {
        for (int column = startIndex; column < countOfDigitsOnOneSide; column++) {
            if (!numbers[row][column].isZero()) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findNotZeroIndexByReverseRow(int row, int startIndex) {
        for (int column = startIndex; column >= 0; column--) {
            if (!numbers[row][column].isZero()) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    void moveDown() {
        for (int column = 0; column < countOfDigitsOnOneSide; column++) {
            Int[] numbersByColumn = getNumbersByColumn(column);
            numbersByColumn = reverseNumbers(numbersByColumn);

            for (int row = 0; row < countOfDigitsOnOneSide - 1; row++) {
                Int currentNumber = numbersByColumn[row];
                if (currentNumber.isZero()) continue;

                getIfSameIgnoreZeroValue(numbersByColumn, row + 1, currentNumber)
                        .ifPresent(nextSameNumber -> {
                            currentNumber.doubleTheValue();
                            nextSameNumber.setValue(0);
                        });
            }
        }

        alignNumbersToBottom();
    }

    private Int[] reverseNumbers(Int[] numbers) {
        Int[] result = new Int[numbers.length];
        int lastIndex = result.length - 1;

        for (int i = numbers.length - 1; i >= 0; i--) {
            result[lastIndex - i] = numbers[i];
        }

        return result;
    }

    private void alignNumbersToBottom() {
        for (int column = 0; column < countOfDigitsOnOneSide; column++) {
            for (int row = 0; row < countOfDigitsOnOneSide; row++) {
                if (!numbers[row][column].isZero()) continue;

                Optional<Integer> notZeroIndex = findNotZeroIndexByReverseColumn(column, row + 1);
                if (notZeroIndex.isPresent()) {
                    swapTwoNumber(row, column, notZeroIndex.get(), column);
                    swapped = true;
                }
            }
        }
    }

    private void swapTwoNumber(int row1, int column1, int row2, int column2) {
        Int temp = numbers[row1][column1];
        numbers[row1][column1] = numbers[row2][column2];
        numbers[row2][column2] = temp;
    }

    void moveLeft() {
        for (int row = 0; row < countOfDigitsOnOneSide; row++) {
            Int[] numbersByRow = getNumbersByRow(row);

            for (int column = 0; column < countOfDigitsOnOneSide - 1; column++) {
                Int currentNumber = numbersByRow[column];
                if (currentNumber.isZero()) continue;

                getIfSameIgnoreZeroValue(numbersByRow, column + 1, currentNumber)
                        .ifPresent(nextSameNumber -> {
                            currentNumber.doubleTheValue();
                            nextSameNumber.setValue(0);
                        });
            }
        }

        alignNumbersToLeft();
    }

    private Int[] getNumbersByRow(int row) {
        Int[] result = new Int[countOfDigitsOnOneSide];
        System.arraycopy(numbers[row], 0, result, 0, result.length);
        return result;
    }

    private void alignNumbersToLeft() {
        for (int row = 0; row < countOfDigitsOnOneSide; row++) {
            for (int column = 0; column < countOfDigitsOnOneSide; column++) {
                if (!numbers[row][column].isZero()) continue;

                Optional<Integer> notZeroIndex = findNotZeroIndexByRow(row, column + 1);
                if (notZeroIndex.isPresent()) {
                    swapTwoNumber(row, column, row, notZeroIndex.get());
                    swapped = true;
                }
            }
        }
    }

    void moveRight() {
        for (int row = 0; row < countOfDigitsOnOneSide; row++) {
            Int[] numbersByRow = getNumbersByRow(row);
            numbersByRow = reverseNumbers(numbersByRow);

            for (int column = 0; column < countOfDigitsOnOneSide - 1; column++) {
                Int currentNumber = numbersByRow[column];
                if (currentNumber.isZero()) continue;

                getIfSameIgnoreZeroValue(numbersByRow, column + 1, currentNumber)
                        .ifPresent(nextSameNumber -> {
                            currentNumber.doubleTheValue();
                            nextSameNumber.setValue(0);
                        });
            }
        }

        alignNumbersToRight();
    }

    private void alignNumbersToRight() {
        for (int row = 0; row < countOfDigitsOnOneSide; row++) {
            for (int column = 0; column < countOfDigitsOnOneSide; column++) {
                if (!numbers[row][column].isZero()) continue;

                Optional<Integer> notZeroIndex = findNotZeroIndexByReverseRow(row, column + 1);
                if (notZeroIndex.isPresent()) {
                    swapTwoNumber(row, column, row, notZeroIndex.get());
                    swapped = true;
                }
            }
        }
    }

    public int[][] getNumbers() {
        int[][] result = new int[countOfDigitsOnOneSide][countOfDigitsOnOneSide];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = numbers[i][j].getValue();
            }
        }
        return result;
    }

    public void newTwo() {
        List<Int> zeroNumber = new ArrayList<>(countOfDigitsOnOneSide * countOfDigitsOnOneSide);
        for (Int[] numbersByRow : numbers) {
            for (Int anInt : numbersByRow) {
                if (anInt.isZero()) {
                    zeroNumber.add(anInt);
                }
            }
        }
        int randomIndex = (int) (Math.random() * zeroNumber.size());
        zeroNumber.get(randomIndex).setValue(2);
    }

    public int calculateCurrentStepScore() {
        int result = 0;

        for (Int[] numbersByRow : numbers) {
            for (Int anInt : numbersByRow) {
                if (anInt.isValueDoubled()) {
                    result += anInt.getValue();
                }
            }
        }

        return result;
    }

    public void resetStatus() {
        for (Int[] numberByRow : numbers) {
            for (Int anInt : numberByRow) {
                anInt.resetStatus();
            }
        }
    }

    public boolean canMove() {
        // todo
        return true;
    }

    public boolean isMoved() {
        for (Int[] numbersByRow : numbers) {
            for (Int anInt : numbersByRow) {
                if (anInt.isValueChanged()) {
                    return true;
                }
            }
        }
        return swapped;
    }

    public void prepareForNextStep() {
        for (Int[] numbersByRow : numbers) {
            for (Int anInt : numbersByRow) {
                anInt.resetStatus();
            }
        }
        swapped = false;
    }

    private static class Int {
        private int value = 0;
        private boolean valueChanged = false;
        private boolean valueDoubled = false;

        private boolean isZero() {
            return value == 0;
        }

        private void doubleTheValue() {
            setValue(value * 2);
            valueDoubled = true;
        }

        private int getValue() {
            return value;
        }

        private void setValue(int value) {
            this.value = value;
            valueChanged = true;
        }

        private boolean isValueChanged() {
            return valueChanged;
        }

        public boolean isValueDoubled() {
            return valueDoubled;
        }

        private void resetStatus() {
            valueChanged = false;
            valueDoubled = false;
        }

        private boolean valueEquals(Int other) {
            return value == other.value;
        }
    }
}
