exports.helloWorld = function helloWorld (req, res) {
  if (req.body.message === undefined) {
    res.status(400).send('err error');
  } else {
    console.log(req.body.message);
    res.status(200).send('Hello World!-version 2');
  }
};
