import { TibcoParserPage } from './app.po';

describe('tibco-parser App', function() {
  let page: TibcoParserPage;

  beforeEach(() => {
    page = new TibcoParserPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
