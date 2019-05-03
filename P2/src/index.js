const tf = require('@tensorflow/tfjs-node');

const SAMPLE_SIZE = 10000;

async function run() {
	const data = tf.data.csv(
		'file://data/train.csv', {
			columnConfigs: {
				rating: {
					isLabel: true
				}
			}
	});

	let drugs = new Map();
	let conditions = new Map();

	const sample = data.take(SAMPLE_SIZE);

	await sample.forEachAsync( ({xs}) => {
		if (!drugs.has(xs.drugName))
			drugs.set(xs.drugName, drugs.size);
		if (!conditions.has(xs.condition))
			conditions.set(xs.condition, conditions.size);
	});

	console.log(`drugs: ${drugs.size}`);
	console.log(`conditions: ${conditions.size}`);

	const numInputs = drugs.size + conditions.size + 1;
	console.log(numInputs);

	const flatDataset = await sample.map( ({xs, ys}) => {
		const drug = tf.oneHot([drugs.get(xs.drugName)], drugs.size).flatten();
		const condition = tf.oneHot([conditions.get(xs.condition)], conditions.size).flatten();
		const drugArr = drug.arraySync();
		const condArr = condition.arraySync();
		const tens = tf.tensor([...drugArr, ...condArr, xs.usefulCount]);
		drug.dispose();
		condition.dispose();
		const normLabel = ys.rating / 10;
		return {xs: tens, ys: [normLabel]}
	}).batch(SAMPLE_SIZE);

	const model = tf.sequential();
	model.add(tf.layers.dense({
		inputShape: [numInputs],
		units: 64,
	}));

	model.add(tf.layers.dense({
		units: 32,
	}));

	model.add(tf.layers.dense({
		units: 1
	}));

	const LEARNING_RATE = 0.001;

	model.compile({
		optimizer: tf.train.rmsprop(LEARNING_RATE),
		loss: 'meanSquaredError',
		metrics: ['accuracy']
	});

	await model.fitDataset(flatDataset, {
		epochs: 10,
		callbacks: {
		  onEpochEnd: async (epoch, logs) => {
			  console.log(logs);
			console.log(epoch + ':' + logs.loss);
		  }
		}
	  });
}

run();