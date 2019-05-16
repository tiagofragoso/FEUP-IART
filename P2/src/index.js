const tf = require('@tensorflow/tfjs-node');

const SAMPLE_SIZE = 160000;

async function run() {
	const data = tf.data.csv(
		'file://data/train.csv', {
			columnConfigs: {
				rating: {
					isLabel: true
				},
			}
		});

	let drugs = new Map();
	let conditions = new Map();

	const sample = data.take(SAMPLE_SIZE);

	await sample.forEachAsync(({ xs }) => {
		if (!drugs.has(xs.drugName))
			drugs.set(xs.drugName, drugs.size);
		if (!conditions.has(xs.condition))
			conditions.set(xs.condition, conditions.size);
	});

	console.log(`drugs: ${drugs.size}`);
	console.log(`conditions: ${conditions.size}`);

	const numInputs = drugs.size + conditions.size + 1;
	console.log(numInputs);

	const mapInput = ({ xs, ys }) => {
		// const drug = tf.oneHot([drugs.get(xs.drugName)], drugs.size).flatten();
		// const condition = tf.oneHot([conditions.get(xs.condition)], conditions.size).flatten();
		// const drugArr = drug.arraySync();
		// const condArr = condition.arraySync();
		// const tens = tf.tensor([...drugArr, ...condArr, xs.usefulCount]);
		// drug.dispose();
		// condition.dispose();
		const tens = [drugs.get(xs.drugName) + 1, conditions.get(xs.condition) + 1, xs.usefulCount];
		//const normLabel = ys.rating / 10;
		let normLabel;
		if (ys.rating <= 4) {
			normLabel = -1;
		} else if (ys.rating <= 7) {
			normLabel = 0;
		} else {
			normLabel = 1;
		}
		return { xs: tens, ys: [normLabel] }
	};

	const flatDataset = await sample.map(mapInput).batch(SAMPLE_SIZE);

	const model = tf.sequential();
	model.add(tf.layers.dense({
		//inputShape: [numInputs],
		inputShape: [3],
		units: 64,
	}));

	model.add(tf.layers.dense({
		units: 1,
		activation: 'sigmoid'
	}));

	const LEARNING_RATE = 0.01;

	model.compile({
		optimizer: tf.train.rmsprop(LEARNING_RATE),
		loss: 'meanSquaredError',
		metrics: ['accuracy']
	});

	const input = mapInput({
		xs: {
			drugName: 'Valsartan', condition: 'Left Ventricular Dysfunction', date: 'May 20, 2012', usefulCount: 27
		},
		ys: {
			rating: 9
		}
	});

	await model.fitDataset(flatDataset, {
		epochs: 50,
		callbacks: {
			onEpochEnd: async (epoch, logs) => {
				console.log(logs);
				console.log(epoch + ':' + logs.loss);
				model.predict(tf.tensor([input.xs])).print();
			}
		}
	});
	
}

run();