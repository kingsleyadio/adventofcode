// url => https://adventofcode.com/2021/day/2

const fs = require('fs');
const calculatePositionAndDepth = () => {
    try {
        const data = fs.readFileSync('./input.txt', 'utf8');
        const arrayValues = data.split('\n');
        let horizontalPosition = 0;
        let depth = 0;

        for (let i = 0; i < arrayValues.length; i++) {
            let [command, value] = arrayValues[i].split(' ');
            value = parseInt(value)

            if(arrayValues[i] != '') {
                if (command == 'forward') {
                    horizontalPosition += value
                }

                if (command == 'down') {
                    depth += value
                }

                if (command == 'up') {
                    depth -= value
                }
            }
        }

        return horizontalPosition * depth;
    } catch (err) {
        console.error(err, 'error reading input file')
    }
}
// console.log(calculatePositionAndDepth());

const calculatePositionAndDepthPart2 = () => {
    try {
        const data = fs.readFileSync('./input.txt', 'utf8');
        const arrayValues = data.split('\n');
        let horizontalPosition = 0;
        let depth = 0;
        let aim = 0;

        for (let i = 0; i < arrayValues.length; i++) {
            let [command, value] = arrayValues[i].split(' ');
            value = parseInt(value)

            if(arrayValues[i] != '') {
                if (command == 'forward') {
                    horizontalPosition += value
                    depth += aim*value
                }

                if (command == 'down') {
                    aim += value
                }

                if (command == 'up') {
                    aim -= value
                }
            }
        }

        return horizontalPosition * depth;
    } catch (err) {
        console.error(err, 'error reading input file')
    }
}


console.log(calculatePositionAndDepthPart2());
