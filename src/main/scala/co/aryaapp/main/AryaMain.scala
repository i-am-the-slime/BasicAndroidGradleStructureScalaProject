package co.aryaapp.main

import android.content.{Intent, Context}
import android.graphics.Color
import android.os.{PersistableBundle, AsyncTask, Bundle}
import android.support.v4.app.{Fragment, FragmentPagerAdapter}
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view
import android.view.{Menu, Gravity, View}
import co.aryaapp.TypedResource._
import co.aryaapp.helpers.AndroidConversions._
import co.aryaapp.helpers.AryaBaseActivity
import co.aryaapp.journal.JournalActivity
import co.aryaapp.main.fragments.{CustomiseFragment, HomeFragment}
import co.aryaapp.{R, TR}
import com.balysv.materialmenu.MaterialMenuDrawable
import com.balysv.materialmenu.MaterialMenuDrawable.Stroke
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import scala.concurrent.ExecutionContext
import scala.language.postfixOps


class AryaMain extends AryaBaseActivity {
  lazy val viewPager = this.findView(TR.mainPager)
  lazy val toolBar = this.findView(TR.mainToolbar)
  lazy val drawerLayout = this.findView(TR.drawerLayout)
  lazy val sideMenuRecyclerView = this.findView(TR.mainMenuRecyclerView)
  lazy val menu = new MaterialMenuIconToolbar(this, Color.WHITE, Stroke.THIN)
                  { override def getToolbarViewId = R.id.mainToolbar }


  class AryaPagerAdapter extends FragmentPagerAdapter(getSupportFragmentManager){
    val fragments = List(
      () => new HomeFragment()
      ,() => new CustomiseFragment()
    )
    override def getItem(i: Int): Fragment = fragments(i)()
    override def getCount: Int = fragments.length
  }


  def setupViewPager() = {
    viewPager.setAdapter(new AryaPagerAdapter)
  }


  def isDrawerOpened = drawerLayout.isDrawerOpen(Gravity.START)
  def setupDrawer() = {
    drawerLayout.setDrawerListener(new SimpleDrawerListener {
      override def onDrawerSlide(drawerView: View, slideOffset: Float): Unit = {
        val offset = if(isDrawerOpened) 2f-slideOffset else slideOffset
        menu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, offset)
      }

      override def onDrawerStateChanged(newState: Int): Unit = {
        if(newState == DrawerLayout.STATE_IDLE) {
          if(isDrawerOpened) menu.setState(MaterialMenuDrawable.IconState.ARROW)
          else menu.setState(MaterialMenuDrawable.IconState.BURGER)
        }
      }
    })
  }


  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    super.onCreateOptionsMenu(menu)
  }


  override def onOptionsItemSelected(item: view.MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_create_new_journal => startNewJournalActivity()
      case _ => super.onOptionsItemSelected(item)
    }
  }


  val FILL_IN_JOURNAL = 1
  def startNewJournalActivity():Boolean = {
    launchActivity[JournalActivity]
    true
  }


  def setupToolbar() = {
    toolBar.setNavigationOnClickListener((v:View) => {
      if(isDrawerOpened)
        drawerLayout.closeDrawer(Gravity.START)
      else
        drawerLayout.openDrawer(Gravity.START)
      menu.animatePressedState(MaterialMenuDrawable.IconState.ARROW)
    }
    )
  }

  def setupSideMenu() = {
    sideMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this))
    sideMenuRecyclerView.setAdapter(new MainMenuAdapter(Seq(
    new MenuItem("First item", "a", (v) => {
      viewPager.setCurrentItem(0, false)
      drawerLayout.closeDrawer(Gravity.START)
    })
    , new MenuItem("Second item", "a", (v) => {
        viewPager.setCurrentItem(1, false)
        drawerLayout.closeDrawer(Gravity.START)
      })
    )))
    sideMenuRecyclerView.setHasFixedSize(true)
  }


  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupViewPager()
    setSupportActionBar(toolBar)
    setupDrawer()
    setupToolbar()
    menu.setNeverDrawTouch(true)
    setupSideMenu()
    startNewJournalActivity()
  }


  override def onSaveInstanceState(outState: Bundle): Unit = {
    menu.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }


  override def onPostCreate(savedInstanceState: Bundle): Unit = {
    super.onPostCreate(savedInstanceState)
    menu.syncState(savedInstanceState)
  }


  override def attachBaseContext(newBase: Context) = {
    super.attachBaseContext(new CalligraphyContextWrapper(newBase))
  }
}
