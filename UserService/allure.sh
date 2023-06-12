cp -r allure-report/history allure-results
allure generate -c
# allure open allure-report
mkdir -p ../docs
rm -rf ../docs
cp -r ./allure-report ../docs
