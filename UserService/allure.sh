cp -r allure-report/history allure-results
allure generate -c
# allure open allure-report
rm -rf ../docs
cp -r ./allure-report ../docs
