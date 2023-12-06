// https://adventofcode.com/2021/day/1

const fs = require('fs')
const countIncreasedMeasurements = () => {
    try {
        const data = fs.readFileSync('./day1_input.txt', 'utf8')
        console.log(data)
    } catch (err) {
        console.error(err, 'error reading input file')
    }
}
