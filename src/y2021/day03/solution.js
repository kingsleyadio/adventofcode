// url: https://adventofcode.com/2021/day/3

const fs = require('fs');

const binaryDiagnostic = () => {
    try {
        const data = fs.readFileSync('./input.txt', 'utf8');
        const arrayValues = data.split('\n');
        const ones = Array(arrayValues[0].length).fill(0);
        const zeros = Array(arrayValues[0].length).fill(0);
        const gamma = [];
        const epsilon = [];

        for (let i = 0; i < arrayValues.length; i++) {
            let elementArr = arrayValues[i].split('');

            // console.log({elementArr})

            for (let j = 0; j < elementArr.length; j++) {
                let element = parseInt(elementArr[j]);
                if(element == 0) {
                    zeros[j] += 1
                }

                if(element == 1) {
                    ones[j] += 1
                }
            }
        }

        for (let i = 0; i < ones.length; i++) {
            if (ones[i] > zeros[i]) {
                gamma[i] = 1;
                epsilon[i] = 0;
            }

            if (zeros[i] > ones[i]) {
                gamma[i] = 0;
                epsilon[i] = 1;
            }
        }

        const gammaDecimalValue = parseInt(gamma.join(''), 2)
        const epsilonDecimalValue = parseInt(epsilon.join(''), 2)

        console.log({ones, zeros, gamma, epsilon})

        return gammaDecimalValue*epsilonDecimalValue;
    } catch (err) {
        console.error(err, 'error reading input file')
    }
}

// console.log(binaryDiagnostic())

const lifeSupportRating = () => {
    try {
        const data = fs.readFileSync('./input.txt', 'utf8');
        const arrayValues = data.split('\n');
        const ones = Array(arrayValues[0].length).fill(0);
        const zeros = Array(arrayValues[0].length).fill(0);
        const gamma = [];
        const epsilon = [];

        for (let i = 0; i < arrayValues.length; i++) {
            let elementArr = arrayValues[i].split('');

            for (let j = 0; j < elementArr.length; j++) {
                let element = parseInt(elementArr[j]);
                if(element == 0) {
                    zeros[j] += 1
                }

                if(element == 1) {
                    ones[j] += 1
                }
            }
        }

        const index = 0;
        const items = []
        console.log({arrayValues})

        const result = arrayValues.map((item, index) => {
            if(item.length != 0){
                let oneValue = ones[index];
                let zeroValue = zeros[index];

                if(oneValue > zeroValue) {

                }
            }
        })

        console.log({result})

        // for (let i = 0; i < ones.length; i++) {
        //     if (ones[i] > zeros[i]) { // 1 is significant
        //         console.log({item: arrayValues})
        //     }
        // }

        // const gammaDecimalValue = parseInt(gamma.join(''), 2)
        // const epsilonDecimalValue = parseInt(epsilon.join(''), 2)

        console.log({ones, zeros})

        // return gammaDecimalValue*epsilonDecimalValue;
    } catch (err) {
        console.error(err, 'error reading input file')
    }
}

console.log(lifeSupportRating())
